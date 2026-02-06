package br.edu.ifpb.pweb2.vox.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.edu.ifpb.pweb2.vox.entity.Assunto;
import br.edu.ifpb.pweb2.vox.repository.AssuntoRepository;

@Service
public class AssuntoService {
    
    @Autowired
    AssuntoRepository assuntoRepository;

    public List<Assunto> findAll() {
        return assuntoRepository.findAll();
    }

    public Page<Assunto> findAll(Pageable pageable) {
        return assuntoRepository.findAll(pageable);
    }

    public Assunto findById(Long id) {
        return assuntoRepository.findById(id).orElse(null);
    }

    public Assunto save(Assunto assunto) {
        return assuntoRepository.save(assunto);
    }

    public void deleteById(Long id) {
        if (assuntoRepository.findById(id) != null) {
            assuntoRepository.deleteById(id);
        } else {
            throw new RuntimeException("Assunto com ID " + id + " não encontrado.");
        }
    }
}
