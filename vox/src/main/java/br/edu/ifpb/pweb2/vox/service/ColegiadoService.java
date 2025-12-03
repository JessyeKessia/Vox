package br.edu.ifpb.pweb2.vox.service;

import br.edu.ifpb.pweb2.vox.entity.Colegiado;
import br.edu.ifpb.pweb2.vox.repository.ColegiadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
<<<<<<< HEAD
import org.springframework.data.domain.Page; // NOVO
import org.springframework.data.domain.Pageable; // NOVO
import org.springframework.stereotype.Service;
=======
import org.springframework.stereotype.Component;
>>>>>>> e08501738ef912fd79693544bc9c5321da2e4082
import java.util.List;

@Component
public class ColegiadoService implements Service<Colegiado, Long> {
    @Autowired
    private ColegiadoRepository colegiadoRepository;

    @Override
    public List<Colegiado> findAll() {
        return colegiadoRepository.findAll();
    }

    // NOVO MÉTODO: Paginação (sem @Override)
    public Page<Colegiado> findAll(Pageable pageable) {
        return colegiadoRepository.findAll(pageable);
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