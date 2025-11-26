package br.edu.ifpb.pweb2.vox.service;

import br.edu.ifpb.pweb2.vox.model.Colegiado;
import br.edu.ifpb.pweb2.vox.repository.ColegiadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ColegiadoService implements br.edu.ifpb.pweb2.vox.service.Service<Colegiado, Long> {
    @Autowired
    private ColegiadoRepository colegiadoRepository;

    @Override
    public List<Colegiado> findAll() {
        return colegiadoRepository.findAll();
    }

    @Override
    public Colegiado findById(Long id) {
        return colegiadoRepository.findById(id).orElse(null);
    }

    @Override
    public Colegiado save(Colegiado colegiado) {
        return colegiadoRepository.save(colegiado);
    }

    @Override
    public void deleteById(Long id) {
        colegiadoRepository.deleteById(id);
    }
}

