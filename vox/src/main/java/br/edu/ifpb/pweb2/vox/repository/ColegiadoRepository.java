package br.edu.ifpb.pweb2.vox.repository;

import br.edu.ifpb.pweb2.vox.model.Colegiado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ColegiadoRepository extends JpaRepository<Colegiado, Long> {
    
}
