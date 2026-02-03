package br.edu.ifpb.pweb2.vox.service;

import br.edu.ifpb.pweb2.vox.entity.*;
import br.edu.ifpb.pweb2.vox.enums.*;
import br.edu.ifpb.pweb2.vox.repository.ReuniaoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional; // Import correto
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

    public Page<Reuniao> findAll(Pageable pageable) {
        return reuniaoRepository.findAll(pageable);
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
        // REQFUNC 10: Garante que apenas uma sessão esteja iniciada por vez
        Optional<Reuniao> sessaoAberta = reuniaoRepository.findByStatus(StatusReuniao.EM_ANDAMENTO);

        if (sessaoAberta.isPresent() && !sessaoAberta.get().getId().equals(id)) {
            throw new RuntimeException("Já existe uma sessão de julgamento iniciada!");
        }

        Reuniao r = reuniaoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Reunião não encontrada"));
        
        r.setStatus(StatusReuniao.EM_ANDAMENTO);
        reuniaoRepository.save(r);
    }

    @Transactional
    public void finalizarSessao(Long reuniaoId) {
        Reuniao r = this.findById(reuniaoId);
        if (r == null) throw new RuntimeException("Reunião não encontrada.");

        // REQFUNC 12: Finaliza e impede alterações
        r.setStatus(StatusReuniao.ENCERRADA);

        for (Processo p : r.getPauta()) {
            this.processarJulgamento(p.getId());
        }
        reuniaoRepository.save(r);
    }

    private void processarJulgamento(Long processoId) {
        Processo processo = processoService.findById(processoId);
        
        // REQFUNC 11: Cálculo automático do resultado
        List<Voto> todosOsVotos = votoService.findAll();

        long votosComRelator = todosOsVotos.stream()
            .filter(v -> v.getVoto() == TipoVoto.COM_RELATOR)
            .count();

        long votosDivergentes = todosOsVotos.stream()
            .filter(v -> v.getVoto() == TipoVoto.DIVERGENTE)
            .count();

        // Se a maioria divergiu, o resultado é o contrário do relator
        if (votosDivergentes > votosComRelator) {
            TipoDecisao resultadoInvertido = (processo.getDecisaoRelator() == TipoDecisao.DEFERIDO) 
                ? TipoDecisao.INDEFERIDO 
                : TipoDecisao.DEFERIDO;
            processo.setDecisaoRelator(resultadoInvertido);
        }

        processo.setStatus(StatusProcesso.JULGADO);
        processoService.save(processo);
    }
}