package br.edu.ifpb.pweb2.vox.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import br.edu.ifpb.pweb2.vox.entity.Usuario;

@Controller
@RequestMapping("/home")
public class HomeController {

    @GetMapping
    public ModelAndView home(@AuthenticationPrincipal Usuario usuarioLogado) {
        ModelAndView mv = new ModelAndView("home/index");
        mv.addObject("usuarioLogado", usuarioLogado);
        return mv;
    }
}

