package br.edu.ifpb.pweb2.vox.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.ifpb.pweb2.vox.entity.Processo;
import br.edu.ifpb.pweb2.vox.entity.Reuniao;
import br.edu.ifpb.pweb2.vox.entity.Usuario;
import br.edu.ifpb.pweb2.vox.entity.Voto;

@Repository
public interface VotoRepository extends JpaRepository<Voto, Long> {
    
    boolean existsByProfessorAndReuniao(Usuario professor, Reuniao reuniao);

    boolean existsByProfessorAndReuniaoAndProcesso(
        Usuario professor, Reuniao reuniao, Processo processo
    );

    // Retorna true se existir algum voto do professor para o processo
    boolean existsByProfessorAndProcesso(Usuario professor, Processo processo);
    
    List<Voto> findByProcessoAndReuniao(Processo processo, Reuniao reuniao);

    long countByReuniao(Reuniao reuniao);

    List<Voto> findByProfessorAndReuniao(Usuario professor, Reuniao reuniao);

}