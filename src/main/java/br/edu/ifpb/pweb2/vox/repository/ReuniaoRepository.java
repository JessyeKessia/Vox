package br.edu.ifpb.pweb2.vox.repository;

import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.edu.ifpb.pweb2.vox.entity.Reuniao;
import br.edu.ifpb.pweb2.vox.enums.StatusReuniao;
import java.util.Optional;

@Repository
public interface ReuniaoRepository extends JpaRepository<Reuniao, Long> {
    // Busca se existe alguma reunião em andamento (apenas uma por vez)
    Optional<Reuniao> findByStatus(StatusReuniao status);
    
}