package br.edu.ifpb.pweb2.vox.controller;

import br.edu.ifpb.pweb2.vox.entity.Colegiado;
import br.edu.ifpb.pweb2.vox.service.ColegiadoService;
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
@RequestMapping("/colegiados")
public class ColegiadoController {
    @Autowired
    private ColegiadoService colegiadoService;

    @Autowired
    private ProfessorService professorService;

    @GetMapping
    public ModelAndView list(@PageableDefault(size = 10, sort = {"descricao"}, direction = Sort.Direction.ASC) Pageable pageable, ModelAndView modelAndView) {
        modelAndView.setViewName("colegiados/list");
        // MUDANÇA: Adiciona o objeto Page paginado
        modelAndView.addObject("colegiadosPaginados", colegiadoService.findAll(pageable));
        return modelAndView;
    }

    @GetMapping("/form")
    public ModelAndView getForm(Colegiado colegiado, ModelAndView model) {
        model.setViewName("colegiados/form");
        model.addObject("colegiado", colegiado);
        model.addObject("professores", professorService.findAll());
        return model;
    }

    @PostMapping
    public ModelAndView add(Colegiado colegiado, RedirectAttributes redirectAttributes, ModelAndView modelAndView) {
        colegiadoService.save(colegiado);
        redirectAttributes.addFlashAttribute("mensagemSucesso", "Colegiado salvo com sucesso!");
        // PRG: Redireciona para a lista após sucesso
        modelAndView.setViewName("redirect:/colegiados");
        return modelAndView;
    }

    @GetMapping("/{id}")
    public ModelAndView editar(@PathVariable("id") Long id, ModelAndView model) {
        model.setViewName("colegiados/form");
        model.addObject("colegiado", colegiadoService.findById(id));
        model.addObject("professores", professorService.findAll());
        return model;
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            colegiadoService.deleteById(id);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Colegiado removido com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao remover colegiado. Pode haver dados relacionados.");
        }
        return "redirect:/colegiados";
    }
}