package br.edu.ifpb.pweb2.vox.controller;

import br.edu.ifpb.pweb2.vox.model.Professor;
import br.edu.ifpb.pweb2.vox.service.ProfessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
    public ModelAndView addProfessor(Professor professor, ModelAndView modelAndView) {
        professorService.save(professor);
        modelAndView.setViewName("professores/list");
        modelAndView.addObject("professores", professorService.findAll());
        return modelAndView;
    }

    @GetMapping
    public ModelAndView list(ModelAndView modelAndView){
        modelAndView.setViewName("professores/list");
        modelAndView.addObject("professores", professorService.findAll());
        return modelAndView;
    }

    @GetMapping("/{id}")
    public ModelAndView getProfessorById(@PathVariable("id") Long id, ModelAndView model) {
        model.addObject("professor", professorService.findById(id));
        model.setViewName("professores/form");
        return model;
    }
}
