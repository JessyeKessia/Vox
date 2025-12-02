package br.edu.ifpb.pweb2.vox.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.edu.ifpb.pweb2.vox.entity.Assunto;
import br.edu.ifpb.pweb2.vox.repository.AssuntoRepository;

@Component
public class AssuntoService implements Service<Assunto, Long> {
    
    @Autowired
    AssuntoRepository assuntoRepository;

    @Override
    public List<Assunto> findAll() {
        return assuntoRepository.findAll();
    }

    @Override
    public Assunto findById(Long id) {
        return assuntoRepository.findById(id).orElse(null);
    }

    @Override
    public Assunto save(Assunto assunto) {
        return assuntoRepository.save(assunto);
    }

    @Override
    public void deleteById(Long id) {
        if (assuntoRepository.findById(id) != null) {
            assuntoRepository.deleteById(id);
        } else {
            throw new RuntimeException("Assunto com ID " + id + " não encontrado.");
        }
    }
}
