package br.edu.ifpb.pweb2.vox.controller;

import br.edu.ifpb.pweb2.vox.model.Coordenador;
import br.edu.ifpb.pweb2.vox.service.CoordenadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/coordenadores")
public class CoordenadorController {
    @Autowired
    private CoordenadorService coordenadorService;

    @GetMapping("/form")
    public ModelAndView getForm(Coordenador coordenador, ModelAndView model) {
        model.setViewName("coordenadores/form");
        model.addObject("coordenador", coordenador);
        return model;
    }

    @PostMapping
    public ModelAndView add(Coordenador coordenador, ModelAndView modelAndView) {
        coordenadorService.save(coordenador);
        modelAndView.setViewName("coordenadores/list");
        modelAndView.addObject("coordenadores", coordenadorService.findAll());
        return modelAndView;
    }

    @GetMapping
    public ModelAndView list(ModelAndView modelAndView){
        modelAndView.setViewName("coordenadores/list");
        modelAndView.addObject("coordenadores", coordenadorService.findAll());
        return modelAndView;
    }

    @GetMapping("/{id}")
    public ModelAndView getById(@PathVariable("id") Long id, ModelAndView model) {
        model.addObject("coordenador", coordenadorService.findById(id));
        model.setViewName("coordenadores/form");
        return model;
    }
}

