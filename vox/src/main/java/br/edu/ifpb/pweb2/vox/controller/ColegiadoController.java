/* package br.edu.ifpb.pweb2.vox.controller;

import br.edu.ifpb.pweb2.vox.entity.Colegiado;
import br.edu.ifpb.pweb2.vox.enums.Role;
import br.edu.ifpb.pweb2.vox.service.ColegiadoService;
import br.edu.ifpb.pweb2.vox.service.ProfessorService;
import br.edu.ifpb.pweb2.vox.service.UsuarioService;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/colegiados")
public class ColegiadoController {
    @Autowired
    private ColegiadoService colegiadoService;

    @Autowired
    private UsuarioService usuarioService;

    // entrega todos os professores para o modelo certinh poder fazer a listagem e associação 
    @ModelAttribute("professores")
    private List<?> listarProfessores() {
        return usuarioService.findAll().stream()
            .filter(u -> u.getRole() == Role.PROFESSOR || u.getRole() == Role.COORDENADOR)
            .toList();
    }
    

    @GetMapping
    public ModelAndView list(ModelAndView modelAndView) {
        modelAndView.setViewName("colegiados/list");
        modelAndView.addObject("colegiados", colegiadoService.findAll());
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
    public ModelAndView addColegiado(@Valid Colegiado colegiado, BindingResult bindingResult, ModelAndView modelAndView) {
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("colegiados/form");
            modelAndView.addObject("colegiado", colegiado);
            return modelAndView;
        }
        colegiadoService.save(colegiado);
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
    public String delete(@PathVariable("id") Long id) {
        // verificação se o colegiado tem processos ativos pode ser feita aqui, se necessário
        colegiadoService.deleteById(id);
        return "redirect:/colegiados";
    }

    
}
 */