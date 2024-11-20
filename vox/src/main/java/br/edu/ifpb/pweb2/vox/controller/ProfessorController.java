package br.edu.ifpb.pweb2.vox.controller;

import br.edu.ifpb.pweb2.vox.entity.Professor;
import br.edu.ifpb.pweb2.vox.service.ProfessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable; // NOVO
import org.springframework.data.domain.Sort; // NOVO
import org.springframework.data.web.PageableDefault; // NOVO
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes; // NOVO

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
    public ModelAndView addProfessor(Professor professor, RedirectAttributes redirectAttributes,
            ModelAndView modelAndView) {
        try {
            professorService.save(professor);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Professor cadastrado com sucesso!");
            // PRG: Redireciona para a lista após sucesso
            modelAndView.setViewName("redirect:/professores");
        } catch (RuntimeException e) {
            // Trata erro de negócio (REQNAOFUNC 5)
            redirectAttributes.addFlashAttribute("mensagemErro", e.getMessage());
            // Redireciona para o formulário (mantendo o ID se for edição)
            if (professor.getId() != null) {
                modelAndView.setViewName("redirect:/professores/" + professor.getId());
            } else {
                modelAndView.setViewName("redirect:/professores/form");
            }
        }
        return modelAndView;
    }

    @GetMapping
    public ModelAndView list(
            @PageableDefault(size = 10, sort = { "nome" }, direction = Sort.Direction.ASC) Pageable pageable,
            ModelAndView modelAndView) {
        modelAndView.setViewName("professores/list");
        // Adiciona o objeto Page paginado
        modelAndView.addObject("professoresPaginados", professorService.findAll(pageable));
        return modelAndView;
    }

    @GetMapping("/{id}")
    public ModelAndView getProfessorById(@PathVariable("id") Long id, ModelAndView model) {
        model.addObject("professor", professorService.findById(id));
        model.setViewName("professores/form");
        return model;
    }

    @GetMapping("/delete/{id}")
    public String deleteProfessor(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            professorService.deleteById(id);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Professor removido com sucesso!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", e.getMessage());
        }
        return "redirect:/professores";
    }

}