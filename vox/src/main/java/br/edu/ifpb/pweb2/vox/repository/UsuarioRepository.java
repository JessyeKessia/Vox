package br.edu.ifpb.pweb2.vox.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.edu.ifpb.pweb2.vox.entity.Usuario;

// repositorio onde vão ficar os usuários guardados no banco
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // os usuários vão ser achados por email
    Usuario findByEmail(String email);

}
