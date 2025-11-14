package br.edu.ifpb.pweb2.vox.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.edu.ifpb.pweb2.vox.model.Processo;
import br.edu.ifpb.pweb2.vox.repository.ProcessoRepository;

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
        return processoRepository.findById(id);
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
    
}