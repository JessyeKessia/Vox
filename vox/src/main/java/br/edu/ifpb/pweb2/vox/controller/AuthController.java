package br.edu.ifpb.pweb2.vox.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/auth")
public class AuthController {

    // pega o formulário de login
    @GetMapping("/login")
    public ModelAndView loginForm() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("auth/login");
        return modelAndView;
    }
}