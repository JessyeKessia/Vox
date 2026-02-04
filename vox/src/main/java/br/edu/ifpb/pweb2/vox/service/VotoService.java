package br.edu.ifpb.pweb2.vox.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifpb.pweb2.vox.entity.Processo;
import br.edu.ifpb.pweb2.vox.entity.Reuniao;
import br.edu.ifpb.pweb2.vox.entity.Usuario;
import br.edu.ifpb.pweb2.vox.entity.Voto;
import br.edu.ifpb.pweb2.vox.repository.VotoRepository;

@Service
public class VotoService {
    
    @Autowired
    private VotoRepository votoRepository;

    public List<Voto> findAll() {
        return votoRepository.findAll();
    }

    public Voto save(Voto voto) {
        return votoRepository.save(voto);
    }

    public void deleteById(Long id) {
        if (votoRepository.findById(id).isPresent()) {
            votoRepository.deleteById(id);
        } else {
            throw new RuntimeException("Voto com ID " + id + " não encontrado.");
        }
    }

    public Voto findById(Long id) {
        return votoRepository.findById(id).orElse(null);
    }
    public boolean professorJaVotou(Usuario usuario, Reuniao reuniao) {
        return votoRepository.existsByProfessorAndReuniao(usuario, reuniao);
    }
    public boolean jaVotouNoProcesso(
        Usuario professor, Reuniao reuniao, Processo processo) {

        return votoRepository
            .existsByProfessorAndReuniaoAndProcesso(
                professor, reuniao, processo
            );
    }

    public boolean todosMembrosVotaram(Reuniao reuniao) {
        int totalMembros = reuniao.getMembrosPresentes().size();

        long professoresQueVotaram = reuniao.getVotos().stream()
            .map(v -> v.getProfessor().getId())
            .distinct()
            .count();

        return professoresQueVotaram == totalMembros;
    }
    public List<Voto> findByProcessoAndReuniao(Processo processo, Reuniao reuniao) {
        return votoRepository.findByProcessoAndReuniao(processo, reuniao);
    }

    public long countByReuniao(Reuniao reuniao) {
        return votoRepository.countByReuniao(reuniao);
    }
    public Map<Long, Voto> buscarVotosDoProfessorNaReuniao(
        Usuario professor, Reuniao reuniao) {

        return votoRepository
            .findByProfessorAndReuniao(professor, reuniao)
            .stream()
            .collect(Collectors.toMap(
                v -> v.getProcesso().getId(),
                v -> v
            ));
    }
    public boolean jaVotou(Usuario professor, Processo processo) {
        return votoRepository.existsByProfessorAndProcesso(professor, processo);
    }

}
