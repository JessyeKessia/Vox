package br.edu.ifpb.pweb2.vox.controller;

import br.edu.ifpb.pweb2.vox.entity.Aluno;
import br.edu.ifpb.pweb2.vox.service.AlunoService;
import br.edu.ifpb.pweb2.vox.util.PasswordUtil;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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
    public ModelAndView addAluno(Aluno aluno, ModelAndView modelAndView) {
        alunoService.save(aluno);
        modelAndView.setViewName("alunos/list");
        modelAndView.addObject("alunos", alunoService.findAll());
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