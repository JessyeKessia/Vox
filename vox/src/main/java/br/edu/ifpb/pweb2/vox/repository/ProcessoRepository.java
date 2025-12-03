package br.edu.ifpb.pweb2.vox.repository;

import org.springframework.stereotype.Repository;

import br.edu.ifpb.pweb2.vox.entity.Processo;
import br.edu.ifpb.pweb2.vox.enums.StatusProcesso;

import org.springframework.data.domain.Page; // NOVO
import org.springframework.data.domain.Pageable; // NOVO
import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
<<<<<<< HEAD
=======
import org.springframework.data.domain.Sort;
>>>>>>> e08501738ef912fd79693544bc9c5321da2e4082

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

<<<<<<< HEAD
    // MUDANÇA: Retorna Page e aceita Pageable
    @Query("SELECT p FROM Processo p WHERE (:assunto IS NULL OR p.assunto.nome = :assunto)")
    Page<Processo> findByAssunto(@Param("assunto") String assunto, Pageable pageable);

    // MUDANÇA: Retorna Page e aceita Pageable
    @Query("SELECT p FROM Processo p WHERE (:status IS NULL OR p.status = :status)")
    Page<Processo> findByStatus(@Param("status") StatusProcesso status, Pageable pageable);

    // MUDANÇA: Retorna Page e aceita Pageable
    @Query("SELECT p FROM Processo p ORDER BY p.dataRecepcao DESC")
    Page<Processo> ordProcessos(Pageable pageable);
}
=======


}
>>>>>>> e08501738ef912fd79693544bc9c5321da2e4082
