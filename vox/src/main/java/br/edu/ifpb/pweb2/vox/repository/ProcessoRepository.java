package br.edu.ifpb.pweb2.vox.repository;

import org.springframework.stereotype.Repository;

import br.edu.ifpb.pweb2.vox.entity.Processo;
import br.edu.ifpb.pweb2.vox.enums.StatusProcesso;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Sort;

@Repository
public interface ProcessoRepository extends JpaRepository<Processo, Long> {


    @Query("""
       SELECT p FROM Processo p
       WHERE (:status IS NULL OR p.status = :status)
       AND (:assunto IS NULL OR p.assunto.nome = :assunto)
       """)
    List<Processo> findByStatusAndAssunto(
        @Param("status") StatusProcesso status,
        @Param("assunto") String assunto,
        Sort sort
    );

    @Query("""
        SELECT p FROM Processo p
        WHERE (:status IS NULL OR p.status = :status)
        AND (:alunoId IS NULL OR p.alunoInteressado.id = :alunoId)
        AND (:relatorId IS NULL OR p.relator.id = :relatorId)
    """)
    List<Processo> findForCoordenador(
        @Param("status") StatusProcesso status,
        @Param("alunoId") Long alunoId,
        @Param("relatorId") Long relatorId
    );



}
