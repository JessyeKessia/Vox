package br.edu.ifpb.pweb2.vox.repository;

import org.springframework.stereotype.Repository;

import br.edu.ifpb.pweb2.vox.entity.Processo;
import br.edu.ifpb.pweb2.vox.types.StatusProcesso;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


@Repository
public interface ProcessoRepository extends JpaRepository<Processo, Integer> {

    // aqui tem uma gambiarra pra aceitar null nos filtros e tbm pra comparar o nome do assunto 
    // Objeto assunto dá erro pq compara objeto com string, por isso usa assunto.nome
    @Query("SELECT p FROM Processo p WHERE (:assunto IS NULL OR p.assunto.nome = :assunto)")
    List<Processo> findByAssunto(@Param("assunto") String assunto);

    @Query("SELECT p FROM Processo p WHERE (:status IS NULL OR p.status = :status)")
    List<Processo> findByStatus(@Param("status") StatusProcesso status);

    @Query("SELECT p FROM Processo p ORDER BY p.dataRecepcao DESC")
    List<Processo> ordProcessos();
}
