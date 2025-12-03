package br.edu.ifpb.pweb2.vox.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.edu.ifpb.pweb2.vox.entity.Professor;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {
    
    boolean existsByEmail(String email);
    
    boolean existsByMatricula(String matricula);

    // pega a lista de professores que não são coordenadores
    @Query("SELECT p FROM Professor p WHERE p.coordenador = false")
    List<Professor> findByCoordenadorFalse();
}
