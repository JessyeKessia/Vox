package br.edu.ifpb.pweb2.vox.repository;

import br.edu.ifpb.pweb2.vox.model.Professor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {
    boolean existsByLogin(String login);
    boolean existsByMatricula(String matricula);
}
