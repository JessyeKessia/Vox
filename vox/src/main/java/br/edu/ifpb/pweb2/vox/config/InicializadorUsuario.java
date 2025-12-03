package br.edu.ifpb.pweb2.vox.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import br.edu.ifpb.pweb2.vox.entity.Usuario;
import br.edu.ifpb.pweb2.vox.repository.UsuarioRepository;
import br.edu.ifpb.pweb2.vox.util.PasswordUtil;
import br.edu.ifpb.pweb2.vox.enums.Role;

@Component
public class InicializadorUsuario implements ApplicationRunner {

    // injetando o repositorio com os usuarios
    @Autowired
    private UsuarioRepository usuarioRepository;


    // sobrecrevendo o metodo para autorizar o admin
    @Override
    public void run(ApplicationArguments args) {

        // Criar administrador apenas se não existir
        if (usuarioRepository.findByEmail("admin@vox.com") == null) {

            Usuario admin = new Usuario();
            admin.setNome("Administrador Sistema");
            admin.setEmail("admin@vox.com");
            admin.setSenha(PasswordUtil.hashPassword("123"));
            //add role de admin ao usuário  
            admin.setRole(Role.ADMIN);

            usuarioRepository.save(admin);

            System.out.println("Usuário ADMIN criado com sucesso!");
        } else {
            System.out.println("Usuário ADMIN já existe.");
        }
    }
}