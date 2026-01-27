package br.edu.ifpb.pweb2.vox.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
