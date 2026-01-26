package br.edu.ifpb.pweb2.vox.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.ifpb.pweb2.vox.entity.Colegiado;

@Repository
public interface ColegiadoRepository extends JpaRepository<Colegiado, Long> {
    
    void deleteById(Long id);

}
