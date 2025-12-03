package br.edu.ifpb.pweb2.vox.controller;

import br.edu.ifpb.pweb2.vox.entity.Aluno;
import br.edu.ifpb.pweb2.vox.service.AlunoService;
import br.edu.ifpb.pweb2.vox.util.PasswordUtil;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import br.edu.ifpb.pweb2.vox.enums.Role;

@Controller
@RequestMapping("/alunos")
public class AlunoController {

    @Autowired
    private AlunoService alunoService;

    // roda antes de qualquer handler e já entrega o aluno
    @ModelAttribute("alunos")
    public List<Aluno> getAlunos() {
        return alunoService.findAll();
    }

    // pega o formulário de cadastro de aluno assim que voce acessa a rota
    @GetMapping("/form")
    public ModelAndView getForm(Aluno aluno, ModelAndView model) {
        model.setViewName("alunos/form");
        model.addObject("aluno", aluno);
        return model;
    }

    // salva o aluno quando você clica em salvar no formulário
    @PostMapping
    public ModelAndView addAluno(@Valid Aluno aluno, BindingResult result, ModelAndView modelAndView, RedirectAttributes redirectAttributes) {

        /// VERIFICAR SE E-MAIL JÁ EXISTE E NÃO É DO MESMO ALUNO ---
        
        // define a role de aluno
        if (aluno.getRole() == null) {
            aluno.setRole(Role.ALUNO);
        }

        // validação manual ou automática
        if (result.hasErrors()) {
            // não deixa sair do forms se tiver algum problema
            modelAndView.setViewName("alunos/form");
            // volta o objeto aluno 
            modelAndView.addObject("aluno", aluno);
            return modelAndView;
        }

        // só hashear se a senha foi fornecida
        if (aluno.getSenha() != null && !aluno.getSenha().isBlank()) {
            aluno.setSenha(PasswordUtil.hashPassword(aluno.getSenha()));
        }
        
        // salva o aluno no repositorio
        alunoService.save(aluno);
        // manda mensagem
        redirectAttributes.addFlashAttribute("mensagem", "Aluno salvo com sucesso.");
        modelAndView.setViewName("redirect:/alunos");
        return modelAndView;
    }

    @GetMapping
    public ModelAndView list(ModelAndView modelAndView){
        modelAndView.setViewName("alunos/list");
        modelAndView.addObject("alunos", alunoService.findAll());
        return modelAndView;
    }

    @GetMapping("/{id}")
    public ModelAndView getAlunoById(@PathVariable("id") Long id, ModelAndView model) {
        model.addObject("aluno", alunoService.findById(id));
        model.setViewName("alunos/form");
        return model;
    }
    
    @GetMapping("/delete/{id}")
    public String deleteAluno(@PathVariable("id") Long id) {
        alunoService.deleteById(id);
        return "redirect:/alunos";
}

}
