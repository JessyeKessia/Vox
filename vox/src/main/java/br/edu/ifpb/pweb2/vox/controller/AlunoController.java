package br.edu.ifpb.pweb2.vox.controller;

import br.edu.ifpb.pweb2.vox.entity.Aluno;
import br.edu.ifpb.pweb2.vox.service.AlunoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/alunos")
public class AlunoController {
    @Autowired
    private AlunoService alunoService;

    @GetMapping("/form")
    public ModelAndView getForm(Aluno aluno, ModelAndView model) {
        model.setViewName("alunos/form");
        model.addObject("aluno", aluno);
        return model;
    }

    @PostMapping
    public ModelAndView addAluno(Aluno aluno, ModelAndView modelAndView) {
        alunoService.save(aluno);
        modelAndView.setViewName("alunos/list");
        modelAndView.addObject("alunos", alunoService.findAll());
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
