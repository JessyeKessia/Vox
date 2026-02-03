package br.edu.ifpb.pweb2.vox.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifpb.pweb2.vox.entity.Processo;
import br.edu.ifpb.pweb2.vox.entity.Usuario;
import br.edu.ifpb.pweb2.vox.enums.StatusProcesso;
import br.edu.ifpb.pweb2.vox.repository.ProcessoRepository;

@Service
public class ProcessoService {
    
    @Autowired
    ProcessoRepository processoRepository;

    public List<Processo> findAll() {
        return processoRepository.findAll();
    }

    public Processo save(Processo processo) {
        return processoRepository.save(processo);
    }

    public void deleteById(Long id) {
        if (processoRepository.findById(id) != null) {
            processoRepository.deleteById(id);
        } else {
            throw new RuntimeException("Processo com ID " + id + " não encontrado.");
        }
    }
    public Processo findById(Long id) {
        return processoRepository.findById(id).orElse(null);
    }

    public List<Processo> findForAlunoProcessos(StatusProcesso status, String assunto, Long alunoInteressadoId) {
        return processoRepository.findByStatusAndAssuntoAndAlunoInteressadoId(status, assunto, alunoInteressadoId);
    }

    public Page<Processo> findForAlunoProcessos(StatusProcesso status, String assunto, Long alunoInteressadoId, Pageable pageable) {
        return processoRepository.findByStatusAndAssuntoAndAlunoInteressadoId(status, assunto, alunoInteressadoId, pageable);
    }
    public List<Processo> findForCoordenadorProcessos(StatusProcesso status, Long alunoInteressadoId, Long relatorId) {
        return processoRepository.findForCoordenador(status, alunoInteressadoId, relatorId);
    }

    public Page<Processo> findForCoordenadorProcessos(StatusProcesso status, Long alunoInteressadoId, Long relatorId, Pageable pageable) {
        return processoRepository.findForCoordenador(status, alunoInteressadoId, relatorId, pageable);
    }

    public List<Processo> findByProfessor(Usuario professor) {
        return processoRepository.findByRelatorId(professor.getId());
    }

    public org.springframework.data.domain.Page<Processo> findByProfessor(Usuario professor, org.springframework.data.domain.Pageable pageable) {
        return processoRepository.findByRelatorId(professor.getId(), pageable);
    }
}