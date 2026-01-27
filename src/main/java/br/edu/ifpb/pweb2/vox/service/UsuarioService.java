package br.edu.ifpb.pweb2.vox.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.edu.ifpb.pweb2.vox.entity.Usuario;
import br.edu.ifpb.pweb2.vox.enums.Role;
import br.edu.ifpb.pweb2.vox.repository.UsuarioRepository;
import jakarta.transaction.Transactional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Usuario findById(Long id) {
        return usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + id));
    }

    public Usuario findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    @Transactional
    public Usuario save(Usuario usuario) {
        
        // Só re-encode se a senha ainda não estiver criptografada
        if (usuario.getId() == null || !usuario.getSenha().startsWith("$2")) {
            usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        }

        return usuarioRepository.save(usuario);
    }

    public List<Usuario> findByRole(Role role) {
        return usuarioRepository.findByRole(role);
    }
    

    @Transactional
    public Usuario edit(Long id, Usuario dadosNovos) {
        Usuario usuario = findById(id);

        usuario.setNome(dadosNovos.getNome());
        usuario.setEmail(dadosNovos.getUsername());
        usuario.setTelefone(dadosNovos.getTelefone());
        usuario.setRole(dadosNovos.getRole());

        // 🔐 Só atualiza a senha se o campo vier preenchido
        if (dadosNovos.getSenha() != null && !dadosNovos.getSenha().isBlank()) {
            usuario.setSenha(passwordEncoder.encode(dadosNovos.getSenha()));
        }

        return usuarioRepository.save(usuario);
    }


    @Transactional
    public void deleteById(Long id) {
        usuarioRepository.deleteById(id);
    }


    public boolean existsByEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public boolean existsByEmailAndIdNot(String email, Long excludeId) {
        return usuarioRepository.existsByEmailAndIdNot(email, excludeId);
    }

    public List<Usuario> findProfUsuarios(List<Long> ids) {
        List<Role> roles = List.of(Role.PROFESSOR);
        return usuarioRepository.findByIdInAndRoleIn(ids, roles);
    }

    public List<Usuario> findCordenUsuarios(List<Long> ids) {
        List<Role> roles = List.of(Role.COORDENADOR);
        return usuarioRepository.findByIdInAndRoleIn(ids, roles);
    }

    public List<Usuario> findUsuariosByIdIn(List<Long> ids) {
        return usuarioRepository.findUsuariosByIdIn(ids);
    }

}

