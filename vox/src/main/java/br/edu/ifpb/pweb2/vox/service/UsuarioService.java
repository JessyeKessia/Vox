package br.edu.ifpb.pweb2.vox.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.edu.ifpb.pweb2.vox.entity.Professor;
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
        usuario.setSenha(passwordEncoder.encode(usuario.getPassword()));

        // Se for PROFESSOR, mantém a flag coordenador
        if (usuario instanceof Professor professor) {
            professor.setCoordenador(professor.isCoordenador());
        }

        // Se NÃO for professor, garante que não é coordenador
        if (!(usuario instanceof Professor)) {
            // nada pra fazer
        }

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario edit(Long id, Usuario dadosNovos) {

        Usuario usuario = findById(id);

        usuario.setNome(dadosNovos.getNome());
        usuario.setEmail(dadosNovos.getUsername());
        usuario.setTelefone(dadosNovos.getTelefone());
        usuario.setRole(dadosNovos.getRole());

        if (usuario instanceof Professor professor && dadosNovos instanceof Professor novoProfessor) {
            professor.setCoordenador(novoProfessor.isCoordenador());
        }

        if (dadosNovos.getSenha() != null && !dadosNovos.getSenha().isBlank()) {
            usuario.setSenha(passwordEncoder.encode(dadosNovos.getSenha()));
        }

        return usuarioRepository.save(usuario);
    }


    @Transactional
    public void delete(Long id) {
        usuarioRepository.deleteById(id);
    }


    public boolean existsByEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public boolean existsByEmailAndIdNot(String email, Long excludeId) {
        return usuarioRepository.existsByEmailAndIdNot(email, excludeId);
    }

    public List<Professor> findProfessoresByIds(List<Long> ids) {
        List<Role> roles = List.of(Role.PROFESSOR, Role.COORDENADOR);
        
        return usuarioRepository.findByIdInAndRoleIn(ids, roles)
            .stream()
            .filter(u -> u instanceof Professor)
            .map(u -> (Professor) u)
            .toList();
    }
}

