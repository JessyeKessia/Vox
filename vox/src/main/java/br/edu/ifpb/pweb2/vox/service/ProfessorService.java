package br.edu.ifpb.pweb2.vox.service;

import br.edu.ifpb.pweb2.vox.entity.Professor;
import br.edu.ifpb.pweb2.vox.repository.ProfessorRepository;
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
public class ProfessorService implements Service<Professor, Long> {
    @Autowired
    private ProfessorRepository professorRepository;

    @Override
    public List<Professor> findAll() {
        return professorRepository.findAll();
    }

    // NOVO MÉTODO: Paginação (sem @Override)
    public Page<Professor> findAll(Pageable pageable) {
        return professorRepository.findAll(pageable);
    }

    @Override
    public Professor findById(Long id) {
        return professorRepository.findById(id).orElse(null);
    }

    @Override
    public Professor save(Professor professor) {
<<<<<<< HEAD
        if (professorRepository.existsByLogin(professor.getLogin())
                || professorRepository.existsByMatricula(professor.getMatricula())) {
            throw new RuntimeException("Login ou matrícula já utilizados");
=======
        if (professorRepository.existsByEmail(professor.getEmail()) || professorRepository.existsByMatricula(professor.getMatricula())) {
            throw new RuntimeException("Email ou matrícula já utilizados");
>>>>>>> e08501738ef912fd79693544bc9c5321da2e4082
        }
        return professorRepository.save(professor);
    }

    @Override
    public void deleteById(Long id) {
        if (professorRepository.existsById(id)) {
            professorRepository.deleteById(id);
        } else {
            throw new RuntimeException("Professor com ID " + id + " não encontrado.");
        }
    }
<<<<<<< HEAD
}
=======

    // pega a lista dos coordenadores
    public List<Professor> findByCoordenadorFalse() {
        return professorRepository.findByCoordenadorFalse();
    }
}

>>>>>>> e08501738ef912fd79693544bc9c5321da2e4082
