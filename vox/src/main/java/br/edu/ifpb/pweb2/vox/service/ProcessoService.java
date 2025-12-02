package br.edu.ifpb.pweb2.vox.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.edu.ifpb.pweb2.vox.entity.Processo;
import br.edu.ifpb.pweb2.vox.enums.StatusProcesso;
import br.edu.ifpb.pweb2.vox.repository.ProcessoRepository;
import org.springframework.data.domain.Sort;

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

    public List<Processo> findForAlunoProcessos(StatusProcesso status, String assunto, boolean ordenarPorDataCriacao) {
        if (ordenarPorDataCriacao) {
            return processoRepository.findByStatusAndAssunto(status, assunto, Sort.by("dataCriacao").descending());
        } else {
            return processoRepository.findByStatusAndAssunto(status, assunto, Sort.unsorted());
        }
    }
    public List<Processo> findForCoordenadorProcessos(StatusProcesso status, Long alunoId, Long relatorId) {
        return processoRepository.findForCoordenador(status, alunoId, relatorId);
    }
}