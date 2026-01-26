package br.edu.ifpb.pweb2.vox.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.edu.ifpb.pweb2.vox.entity.Colegiado;
import br.edu.ifpb.pweb2.vox.entity.Usuario;

@Repository
public interface ColegiadoRepository extends JpaRepository<Colegiado, Long> {
    
    void deleteById(Long id);

    @Query("select c.membros from Colegiado c where c.id = :colegiadoId")
    List<Usuario> findMembrosByColegiadoId(@Param("colegiadoId") Long colegiadoId);


}
