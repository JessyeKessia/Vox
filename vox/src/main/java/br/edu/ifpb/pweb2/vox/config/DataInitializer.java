package br.edu.ifpb.pweb2.vox.config;

import br.edu.ifpb.pweb2.vox.entity.Usuario;
import br.edu.ifpb.pweb2.vox.enums.Role;

import br.edu.ifpb.pweb2.vox.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer implements CommandLineRunner {

  @Autowired
  private UsuarioService usuarioService;

  @Override
  public void run(String... args) {

    String adminEmail = "admin@vox.com";

    if (usuarioService.findByEmail(adminEmail) == null) {

      Usuario admin = new Usuario();
      admin.setNome("Jessye Pereira");
      admin.setEmail(adminEmail);
      admin.setSenha("admin123");
      admin.setTelefone("83988747097");
      admin.setRole(Role.ADMIN);

      usuarioService.save(admin);

      System.out.println("Usuário ADMIN criado com sucesso!");
    } else {
      System.out.println("Usuário ADMIN já existe.");
    }
  }
}
