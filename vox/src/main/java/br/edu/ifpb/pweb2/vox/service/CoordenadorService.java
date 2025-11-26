package br.edu.ifpb.pweb2.vox.service;

import br.edu.ifpb.pweb2.vox.model.Coordenador;
import br.edu.ifpb.pweb2.vox.repository.CoordenadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CoordenadorService implements br.edu.ifpb.pweb2.vox.service.Service<Coordenador, Long> {
    @Autowired
    private CoordenadorRepository coordenadorRepository;

    @Override
    public List<Coordenador> findAll() {
        return coordenadorRepository.findAll();
    }

    @Override
    public Coordenador findById(Long id) {
        return coordenadorRepository.findById(id).orElse(null);
    }

    @Override
    public Coordenador save(Coordenador coordenador) {
        if (coordenadorRepository.existsByLogin(coordenador.getLogin())) {
            throw new RuntimeException("Login já utilizado");
        }
        return coordenadorRepository.save(coordenador);
    }

    @Override
    public void deleteById(Long id) {
        if (coordenadorRepository.existsById(id)) {
            coordenadorRepository.deleteById(id);
        } else {
            throw new RuntimeException("Coordenador com ID " + id + " não encontrado.");
        }
    }
}

