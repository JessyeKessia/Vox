package br.edu.ifpb.pweb2.vox.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import br.edu.ifpb.pweb2.vox.entity.Assunto;
import br.edu.ifpb.pweb2.vox.repository.AssuntoRepository;

@Component
<<<<<<< HEAD
public class AssuntoService implements Service<Assunto, Integer> {

=======
public class AssuntoService implements Service<Assunto, Long> {
    
>>>>>>> e08501738ef912fd79693544bc9c5321da2e4082
    @Autowired
    AssuntoRepository assuntoRepository;

    @Override
    public List<Assunto> findAll() {
        return assuntoRepository.findAll();
    }

    // NOVO MÉTODO: Paginação (sem @Override)
    public Page<Assunto> findAll(Pageable pageable) {
        return assuntoRepository.findAll(pageable);
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