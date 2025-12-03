package br.edu.ifpb.pweb2.vox.service;

import br.edu.ifpb.pweb2.vox.entity.Professor;
import br.edu.ifpb.pweb2.vox.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProfessorService implements Service<Professor, Long> {
    @Autowired
    private ProfessorRepository professorRepository;

    @Override
    public List<Professor> findAll() {
        return professorRepository.findAll();
    }

    @Override
    public Professor findById(Long id) {
        return professorRepository.findById(id).orElse(null);
    }

    @Override
    public Professor save(Professor professor) {
        if (professorRepository.existsByEmail(professor.getEmail()) || professorRepository.existsByMatricula(professor.getMatricula())) {
            throw new RuntimeException("Email ou matrícula já utilizados");
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

    // pega a lista dos coordenadores
    public List<Professor> findByCoordenadorFalse() {
        return professorRepository.findByCoordenadorFalse();
    }

}

