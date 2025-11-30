package br.edu.ifpb.pweb2.vox.service;

import java.io.ObjectInputFilter.Status;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.edu.ifpb.pweb2.vox.entity.Processo;
import br.edu.ifpb.pweb2.vox.repository.ProcessoRepository;
import br.edu.ifpb.pweb2.vox.types.StatusProcesso;

@Component
public class ProcessoService implements Service<Processo, Integer> {
    
    @Autowired
    ProcessoRepository processoRepository;

    @Override
    public List<Processo> findAll() {
        return processoRepository.findAll();
    }

    @Override
    public Processo findById(Integer id) {
        return processoRepository.findById(id).orElse(null);
    }

    @Override
    public Processo save(Processo processo) {
        return processoRepository.save(processo);
    }

    @Override
    public void deleteById(Integer id) {
        if (processoRepository.findById(id) != null) {
            processoRepository.deleteById(id);
        } else {
            throw new RuntimeException("Processo com ID " + id + " não encontrado.");
        }
    }

    public List<Processo> findByAssunto(String assunto) {
        return processoRepository.findByAssunto(assunto);
    }

    public List<Processo> findByStatus(StatusProcesso status) {
        return processoRepository.findByStatus(status);
    }

    // ordenar processos por data de criação
    public List<Processo> findAllOrderedByCreationDate() {
        return processoRepository.ordProcessos();
    }
    
}