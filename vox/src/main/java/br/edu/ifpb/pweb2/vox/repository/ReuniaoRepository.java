package br.edu.ifpb.pweb2.vox.repository;

import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.edu.ifpb.pweb2.vox.entity.Reuniao;
import br.edu.ifpb.pweb2.vox.enums.StatusReuniao;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReuniaoRepository extends JpaRepository<Reuniao, Long> {
    // Busca se existe alguma reunião em andamento (apenas uma por vez)
    Optional<Reuniao> findByStatus(StatusReuniao status);

    @Query("""
        SELECT r FROM Reuniao r
        WHERE (:colegiadoId IS NULL OR r.colegiado.id = :colegiadoId)
        AND (:status IS NULL OR r.status = :status)
    """)
    List<Reuniao> findFiltrReuniaos(
        @Param("colegiadoId") Long colegiadoId,
        @Param("status") StatusReuniao status
    );

    boolean existsByStatus(StatusReuniao status);

}
