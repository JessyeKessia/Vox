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

    String professorEmail = "professor@vox.com";

    if (usuarioService.findByEmail(professorEmail) == null) {

      Usuario professor = new Usuario();
      professor.setNome("Paulo Professor");
      professor.setEmail(professorEmail);
      professor.setSenha("prof123");
      professor.setTelefone("83999998888");
      professor.setRole(Role.PROFESSOR);

      usuarioService.save(professor);

      System.out.println("Usuário PROFESSOR criado com sucesso!");
    } else {
      System.out.println("Usuário PROFESSOR já existe.");
    }

    String alunoEmail = "aluno@vox.com";

    if (usuarioService.findByEmail(alunoEmail) == null) {

      Usuario aluno = new Usuario();
      aluno.setNome("Ana Aluna");
      aluno.setEmail(alunoEmail);
      aluno.setSenha("aluno123");
      aluno.setTelefone("83977776666");
      aluno.setRole(Role.ALUNO);

      usuarioService.save(aluno);

      System.out.println("Usuário ALUNO criado com sucesso!");
    } else {
      System.out.println("Usuário ALUNO já existe.");
    }

    String coordenadorEmail = "coordenador@vox.com";

    if (usuarioService.findByEmail(coordenadorEmail) == null) {

      Usuario coordenador = new Usuario();
      coordenador.setNome("Carla Coordenadora");
      coordenador.setEmail(coordenadorEmail);
      coordenador.setSenha("coord123");
      coordenador.setTelefone("83966665555");
      coordenador.setRole(Role.COORDENADOR);

      usuarioService.save(coordenador);

      System.out.println("Usuário COORDENADOR criado com sucesso!");
    } else {
      System.out.println("Usuário COORDENADOR já existe.");
    }
  }

}
