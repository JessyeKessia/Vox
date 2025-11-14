package br.edu.ifpb.pweb2.vox.repository;

import org.springframework.stereotype.Repository;
import br.edu.ifpb.pweb2.vox.model.Processo;
import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface ProcessoRepository extends JpaRepository<Processo, Integer> {

}
