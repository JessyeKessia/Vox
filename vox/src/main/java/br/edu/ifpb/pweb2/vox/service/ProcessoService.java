package br.edu.ifpb.pweb2.vox.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.edu.ifpb.pweb2.vox.entity.Processo;
import br.edu.ifpb.pweb2.vox.entity.Professor;
import br.edu.ifpb.pweb2.vox.enums.StatusProcesso;
import br.edu.ifpb.pweb2.vox.repository.ProcessoRepository;

@Component
public class ProcessoService implements Service<Processo, Long> {
    
    @Autowired
    ProcessoRepository processoRepository;

    @Override
    public List<Processo> findAll() {
        return processoRepository.findAll();
    }

    @Override
    public Processo save(Processo processo) {
        return processoRepository.save(processo);
    }

    @Override
    public void deleteById(Long id) {
        if (processoRepository.findById(id) != null) {
            processoRepository.deleteById(id);
        } else {
            throw new RuntimeException("Processo com ID " + id + " não encontrado.");
        }
    }
    @Override
    public Processo findById(Long id) {
        return processoRepository.findById(id).orElse(null);
    }

    public List<Processo> findForAlunoProcessos(StatusProcesso status, String assunto, Long alunoInteressadoId) {
        return processoRepository.findByStatusAndAssuntoAndAlunoInteressadoId(status, assunto, alunoInteressadoId);
    }
    public List<Processo> findForCoordenadorProcessos(StatusProcesso status, Long alunoInteressadoId, Long relatorId) {
        return processoRepository.findForCoordenador(status, alunoInteressadoId, relatorId);
    }

    public List<Processo> findByProfessor(Professor professor) {
        return processoRepository.findByRelatorId(professor.getId());
    }
}