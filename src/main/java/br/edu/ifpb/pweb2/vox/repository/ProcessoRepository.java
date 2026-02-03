package br.edu.ifpb.pweb2.vox.repository;

import org.springframework.stereotype.Repository;

import br.edu.ifpb.pweb2.vox.entity.Processo;
import br.edu.ifpb.pweb2.vox.enums.StatusProcesso;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface ProcessoRepository extends JpaRepository<Processo, Long> {


    @Query("""
       SELECT p FROM Processo p
       WHERE (:status IS NULL OR p.status = :status)
       AND (:assunto IS NULL OR p.assunto.nome = :assunto)
       AND (:alunoInteressadoId IS NULL OR p.alunoInteressado.id = :alunoInteressadoId)
       ORDER BY p.dataCriacao DESC
       """)
    List<Processo> findByStatusAndAssuntoAndAlunoInteressadoId(
        @Param("status") StatusProcesso status,
        @Param("assunto") String assunto,
        @Param("alunoInteressadoId") Long alunoInteressadoId
    );

    @Query(value = """
       SELECT p FROM Processo p
       WHERE (:status IS NULL OR p.status = :status)
       AND (:assunto IS NULL OR p.assunto.nome = :assunto)
       AND (:alunoInteressadoId IS NULL OR p.alunoInteressado.id = :alunoInteressadoId)
       ORDER BY p.dataCriacao DESC
       """,
       countQuery = """
       SELECT count(p) FROM Processo p
       WHERE (:status IS NULL OR p.status = :status)
       AND (:assunto IS NULL OR p.assunto.nome = :assunto)
       AND (:alunoInteressadoId IS NULL OR p.alunoInteressado.id = :alunoInteressadoId)
       """
    )
    Page<Processo> findByStatusAndAssuntoAndAlunoInteressadoId(
        @Param("status") StatusProcesso status,
        @Param("assunto") String assunto,
        @Param("alunoInteressadoId") Long alunoInteressadoId,
        Pageable pageable
    );

    @Query("""
        SELECT p FROM Processo p
        WHERE (:status IS NULL OR p.status = :status)
        AND (:alunoInteressadoId IS NULL OR p.alunoInteressado.id = :alunoInteressadoId)
        AND (:relatorId IS NULL OR p.relator.id = :relatorId)
        """)
    List<Processo> findForCoordenador(
        @Param("status") StatusProcesso status,
        @Param("alunoInteressadoId") Long alunoInteressadoId,
        @Param("relatorId") Long relatorId
    );

    @Query(value = """
        SELECT p FROM Processo p
        WHERE (:status IS NULL OR p.status = :status)
        AND (:alunoInteressadoId IS NULL OR p.alunoInteressado.id = :alunoInteressadoId)
        AND (:relatorId IS NULL OR p.relator.id = :relatorId)
        ORDER BY p.dataCriacao DESC
        """,
        countQuery = """
        SELECT count(p) FROM Processo p
        WHERE (:status IS NULL OR p.status = :status)
        AND (:alunoInteressadoId IS NULL OR p.alunoInteressado.id = :alunoInteressadoId)
        AND (:relatorId IS NULL OR p.relator.id = :relatorId)
        """
    )
    Page<Processo> findForCoordenador(
        @Param("status") StatusProcesso status,
        @Param("alunoInteressadoId") Long alunoInteressadoId,
        @Param("relatorId") Long relatorId,
        Pageable pageable
    );

    @Query("SELECT p FROM Processo p WHERE p.relator.id = :professorId")
    List<Processo> findByRelatorId(@Param("professorId") Long professorId);

    // pageable version for professor's processes
    Page<Processo> findByRelatorId(Long professorId, Pageable pageable);


}
