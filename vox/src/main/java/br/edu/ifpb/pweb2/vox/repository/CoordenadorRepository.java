package br.edu.ifpb.pweb2.vox.repository;

import br.edu.ifpb.pweb2.vox.model.Coordenador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoordenadorRepository extends JpaRepository<Coordenador, Long> {
    boolean existsByLogin(String login);
}
