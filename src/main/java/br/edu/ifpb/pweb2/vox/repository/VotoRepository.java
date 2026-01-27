package br.edu.ifpb.pweb2.vox.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.ifpb.pweb2.vox.entity.Voto;

@Repository
public interface VotoRepository extends JpaRepository<Voto, Long> {
    
}