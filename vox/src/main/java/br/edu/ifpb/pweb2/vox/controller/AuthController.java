package br.edu.ifpb.pweb2.vox.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifpb.pweb2.vox.entity.Usuario;
import br.edu.ifpb.pweb2.vox.repository.UsuarioRepository;
import br.edu.ifpb.pweb2.vox.util.PasswordUtil;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/auth")
public class AuthController {

  @Autowired
  private UsuarioRepository usuarioRepository;

  // pega o formulário de login
  @GetMapping
  public ModelAndView loginForm() {
    ModelAndView modelAndView = new ModelAndView();
    modelAndView.setViewName("auth/login");
    modelAndView.addObject("usuario", new Usuario());
    return modelAndView;
  }

  // faz a autenticação do usuário com o login
  @PostMapping
  public ModelAndView login(Usuario userLogin, HttpSession session, RedirectAttributes redirectAttrs) {

    // pega o usuário pelo o email fornecido
    Usuario usuario = usuarioRepository.findByEmail(userLogin.getEmail());

    // se o usuário existir e a senha tiver certinha, loga o usuario
    if (usuario != null && PasswordUtil.checkPass(userLogin.getSenha(), usuario.getSenha())) {
      session.setAttribute("usuario", usuario);
      // redireciona para o home certinho
      return new ModelAndView("redirect:/home");
    }
    // senão n deixa entrar
    redirectAttrs.addFlashAttribute("mensagem", "Email ou senha inválidos!");
    return new ModelAndView("redirect:/auth");
  }

  // faz o logout do usuario
  @GetMapping("/logout")
  public ModelAndView logout(HttpSession session) {
    session.invalidate();
    return new ModelAndView("redirect:/auth");
  }
}
