package br.edu.ifpb.pweb2.vox.controller;

import br.edu.ifpb.pweb2.vox.entity.Aluno;
import br.edu.ifpb.pweb2.vox.service.AlunoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public ModelAndView addAluno(Aluno aluno, RedirectAttributes redirectAttributes, ModelAndView modelAndView) {
        try {
            alunoService.save(aluno);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Aluno cadastrado com sucesso!");
            // PRG: Redireciona para a lista após sucesso
            modelAndView.setViewName("redirect:/alunos");
        } catch (RuntimeException e) {
            // Trata erro de negócio (REQNAOFUNC 5)
            redirectAttributes.addFlashAttribute("mensagemErro", e.getMessage());
            // Redireciona para o formulário, mantendo o objeto (para edição ou novo
            // cadastro)
            if (aluno.getId() != null) {
                modelAndView.setViewName("redirect:/alunos/" + aluno.getId());
            } else {
                modelAndView.setViewName("redirect:/alunos/form");
            }
        }
        return modelAndView;
    }

    @GetMapping
    public ModelAndView list(
            @PageableDefault(size = 10, sort = { "nome" }, direction = Sort.Direction.ASC) Pageable pageable,
            ModelAndView modelAndView) {
        modelAndView.setViewName("alunos/list");
        // Adiciona o objeto Page paginado
        modelAndView.addObject("alunosPaginados", alunoService.findAll(pageable));
        return modelAndView;
    }

    @GetMapping("/{id}")
    public ModelAndView getAlunoById(@PathVariable("id") Long id, ModelAndView model) {
        model.addObject("aluno", alunoService.findById(id));
        model.setViewName("alunos/form");
        return model;
    }

    @GetMapping("/delete/{id}")
    public String deleteAluno(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            alunoService.deleteById(id);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Aluno removido com sucesso!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", e.getMessage());
        }
        return "redirect:/alunos";
    }

}