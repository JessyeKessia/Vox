package br.edu.ifpb.pweb2.vox.repository;

import br.edu.ifpb.pweb2.vox.model.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    boolean existsByLogin(String login);
    boolean existsByMatricula(String matricula);
}
