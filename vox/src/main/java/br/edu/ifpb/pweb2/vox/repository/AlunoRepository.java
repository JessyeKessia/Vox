package br.edu.ifpb.pweb2.vox.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpb.pweb2.vox.entity.Aluno;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {

    Aluno findByEmail(String email);

    boolean existsByEmail(String email);
    
    boolean existsByMatricula(String matricula);
}
