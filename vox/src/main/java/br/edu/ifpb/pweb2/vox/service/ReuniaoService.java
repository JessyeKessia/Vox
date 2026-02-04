package br.edu.ifpb.pweb2.vox.service;

import br.edu.ifpb.pweb2.vox.entity.*;
import br.edu.ifpb.pweb2.vox.enums.*;
import br.edu.ifpb.pweb2.vox.repository.ReuniaoRepository;
import jakarta.transaction.Transactional;

import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional; 

@Service
public class ReuniaoService {

    @Autowired
    private ReuniaoRepository reuniaoRepository;

    @Autowired
    private ProcessoService processoService;

    @Autowired
    private VotoService votoService;

    public List<Reuniao> findAll() { 
        return reuniaoRepository.findAll(); 
    }

    public Reuniao findById(Long id) { 
        return reuniaoRepository.findById(id).orElse(null); 
    }

    @Transactional
    public void save(Reuniao r) { 
        reuniaoRepository.save(r); 
    }

    @Transactional
    public void iniciarSessao(Long id) {

        Optional<Reuniao> sessaoAberta =
            reuniaoRepository.findByStatus(StatusReuniao.EM_ANDAMENTO);

        if (sessaoAberta.isPresent() && !sessaoAberta.get().getId().equals(id)) {
            throw new RuntimeException("Já existe uma sessão em andamento.");
        }

        Reuniao r = this.findById(id);
        if (r == null) {
            throw new RuntimeException("Reunião não encontrada.");
        }

        r.setStatus(StatusReuniao.EM_ANDAMENTO);
        reuniaoRepository.save(r);
    }

    @Transactional
    public void finalizarSessao(Long reuniaoId) {

        Reuniao reuniao = this.findById(reuniaoId);
        if (reuniao == null) {
            throw new RuntimeException("Reunião não encontrada.");
        }

        if (!reuniao.getStatus().equals(StatusReuniao.EM_ANDAMENTO)) {
            throw new RuntimeException("A reunião não está em andamento.");
        }

        // 🔁 GERA VOTOS AUSENTES AUTOMATICAMENTE
        for (Processo processo : reuniao.getPauta()) {
            gerarVotosAusentes(processo, reuniao);
            processarJulgamento(processo, reuniao);
        }

        reuniao.setStatus(StatusReuniao.ENCERRADA);
        reuniaoRepository.save(reuniao);
    }

    @Transactional
    private void processarJulgamento(Processo processo, Reuniao reuniao) {

        List<Voto> votos =
            votoService.findByProcessoAndReuniao(processo, reuniao);

        long comRelator = votos.stream()
            .filter(v -> v.getTipoVoto() == TipoVoto.COM_RELATOR)
            .count();

        long divergentes = votos.stream()
            .filter(v -> v.getTipoVoto() == TipoVoto.DIVERGENTE)
            .count();

        long ausentes = votos.stream()
            .filter(v -> v.getTipoVoto() == TipoVoto.AUSENTE)
            .count();

        // regra de maioria
        TipoDecisao decisaoFinal;

        if (divergentes > comRelator) {
            decisaoFinal =
                processo.getDecisaoRelator() == TipoDecisao.DEFERIDO
                    ? TipoDecisao.INDEFERIDO
                    : TipoDecisao.DEFERIDO;
        } else {
            decisaoFinal = processo.getDecisaoRelator();
        }

        processo.setDecisaoFinal(decisaoFinal);
        processo.setStatus(StatusProcesso.JULGADO);

        processoService.save(processo);
    }

    /* =========================
       REGRAS AUXILIARES
       ========================= */

    public boolean todosMembrosVotaram(Reuniao reuniao) {

        int membros = reuniao.getMembrosPresentes().size();
        int processos = reuniao.getPauta().size();

        long votosRegistrados =
            votoService.countByReuniao(reuniao);

        return votosRegistrados == (long) membros * processos;
    }

    public List<Reuniao> findFiltros(Long colegiadoId, StatusReuniao status) {
        return reuniaoRepository.findFiltrReuniaos(colegiadoId, status);
    }

    public boolean existeReuniaoEmAndamento() {
        return reuniaoRepository.existsByStatus(StatusReuniao.EM_ANDAMENTO);
    }
    @Transactional
    private void gerarVotosAusentes(Processo processo, Reuniao reuniao) {

        Set<Usuario> membros = reuniao.getMembrosPresentes();

        for (Usuario membro : membros) {

            boolean jaVotou =
                votoService.jaVotouNoProcesso(
                    membro, reuniao, processo
                );

            if (!jaVotou) {
                Voto voto = new Voto();
                voto.setProcesso(processo);
                voto.setReuniao(reuniao);
                voto.setProfessor(membro);
                voto.setTipoVoto(TipoVoto.AUSENTE);

                votoService.save(voto);
            }
        }
    }

}