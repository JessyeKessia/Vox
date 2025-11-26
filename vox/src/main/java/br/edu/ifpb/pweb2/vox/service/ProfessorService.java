package br.edu.ifpb.pweb2.vox.service;

import br.edu.ifpb.pweb2.vox.model.Professor;
import br.edu.ifpb.pweb2.vox.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProfessorService implements br.edu.ifpb.pweb2.vox.service.Service<Professor, Long> {
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
        if (professorRepository.existsByLogin(professor.getLogin()) || professorRepository.existsByMatricula(professor.getMatricula())) {
            throw new RuntimeException("Login ou matrícula já utilizados");
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
}

