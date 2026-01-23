/* package br.edu.ifpb.pweb2.vox.controller;

import br.edu.ifpb.pweb2.vox.entity.Professor;
import br.edu.ifpb.pweb2.vox.enums.Role;
import br.edu.ifpb.pweb2.vox.service.ProfessorService;
import br.edu.ifpb.pweb2.vox.util.PasswordUtil;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/professores")
public class ProfessorController {

    @Autowired
    private ProfessorService professorService;

    @GetMapping("/form")
    public ModelAndView getForm(Professor professor, ModelAndView model) {
        model.setViewName("professores/form");
        model.addObject("professor", professor);
        return model;
    }

    @PostMapping
    public ModelAndView addProfessor(@Valid Professor professor,  BindingResult result, ModelAndView modelAndView) {
    
        // validação manual ou automática
        if (result.hasErrors()) {
            // não deixa sair do forms se tiver algum problema
            modelAndView.setViewName("professores/form");
            // volta o objeto professor 
            modelAndView.addObject("professor", professor);
            return modelAndView;
        } else {
                // define a role padrão do professor
            if (professor.getRole() == null) {
                if (professor.isCoordenador()) {
                    professor.setRole(Role.COORDENADOR);
                } else {
                    professor.setRole(Role.PROFESSOR);
                }
            }

            // só hashear se a senha foi fornecida
            if (professor.getSenha() != null && !professor.getSenha().isBlank()) {
                professor.setSenha(PasswordUtil.hashPassword(professor.getSenha()));
            }

            professorService.save(professor);
            modelAndView.setViewName("redirect:/professores");
            return modelAndView;
        }
    }

    @GetMapping
    public ModelAndView list(ModelAndView modelAndView){
        modelAndView.setViewName("professores/list");
        modelAndView.addObject("professores", professorService.findAll());
        return modelAndView;
    }

    @GetMapping("/{id}/edit")
    public ModelAndView getProfessorById(@PathVariable("id") Long id, ModelAndView model) {
        model.addObject("professor", professorService.findById(id));
        model.setViewName("professores/form");
        return model;
    }
    @GetMapping("/{id}/delete")
    public String deleteProfessor(@PathVariable("id") Long id) {
        professorService.deleteById(id);
        return "redirect:/professores";
}

}
 */