package br.edu.ifpb.pweb2.vox.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.ifpb.pweb2.vox.entity.Usuario;
import br.edu.ifpb.pweb2.vox.enums.Role;

// repositorio onde vão ficar os usuários guardados no banco
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Usuario findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);

    List<Usuario> findByRole(Role role);

    // pega só usuários com papel PROFESSOR ou COORDENADOR pelos IDs
    List<Usuario> findByIdInAndRoleIn(List<Long> ids, List<Role> roles);
}
