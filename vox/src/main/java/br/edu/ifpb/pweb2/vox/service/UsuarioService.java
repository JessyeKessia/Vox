package br.edu.ifpb.pweb2.vox.service;

import br.edu.ifpb.pweb2.vox.repository.UsuarioRepository;
import br.edu.ifpb.pweb2.vox.entity.Usuario;
import br.edu.ifpb.pweb2.vox.enums.Role;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UsuarioService implements Service<Usuario, Long> {

  @Autowired
  private UsuarioRepository usuarioRepository;

  @Override
  public List<Usuario> findAll() {
    return usuarioRepository.findAll();
  }

  @Override
  public Usuario findById(Long id) {
    return usuarioRepository.findById(id).orElse(null);
  }

  // salva o usuario no banco
  @Override
  public Usuario save(Usuario usuario) {
    return usuarioRepository.save(usuario);
  }

  @Override
  public void deleteById(Long id) {
    if (usuarioRepository.existsById(id)) {
      usuarioRepository.deleteById(id);
    } else {
      throw new RuntimeException("Usuário com ID " + id + " não encontrado.");
    }
  }

  // acha o usuário pelo email
  public Usuario findByEmail(String email) {
    return usuarioRepository.findByEmail(email);
  }

  // adicionar uma role a um usuario
  public void addRole(Usuario usuario, String role) {
    // seta a rola, mas antes converte a string para o enum
    usuario.setRole(Role.valueOf(role));
    usuarioRepository.save(usuario);
  }

  // checa se o usuário é admin
  public boolean isAdmin(Usuario usuario) {
    return usuario.hasRole("ADMIN");
  }
}
