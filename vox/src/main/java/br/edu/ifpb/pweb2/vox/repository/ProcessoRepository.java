package br.edu.ifpb.pweb2.vox.repository;

import org.springframework.stereotype.Repository;

import br.edu.ifpb.pweb2.vox.entity.Processo;
import br.edu.ifpb.pweb2.vox.types.StatusProcesso;

import org.springframework.data.domain.Page; // NOVO
import org.springframework.data.domain.Pageable; // NOVO
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface ProcessoRepository extends JpaRepository<Processo, Integer> {

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