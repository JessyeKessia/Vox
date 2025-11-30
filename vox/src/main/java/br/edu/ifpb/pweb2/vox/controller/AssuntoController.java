package br.edu.ifpb.pweb2.vox.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifpb.pweb2.vox.entity.Assunto;
import br.edu.ifpb.pweb2.vox.service.AssuntoService;

@Controller
@RequestMapping("/assuntos")
public class AssuntoController {

    @Autowired
    private AssuntoService assuntoService;

    @GetMapping("/form")
    public ModelAndView getForm(ModelAndView modelAndView) {

        modelAndView.setViewName("assuntos/form"); // view: src/main/resources/templates/assuntos/form.html
        modelAndView.addObject("assunto", new Assunto()); 
        return modelAndView;
    }

    // Salva um novo assunto
    @PostMapping
    public ModelAndView save(Assunto assunto, RedirectAttributes redirectAttributes, ModelAndView model) {
        // salvou o assunto so serviço
        assuntoService.save(assunto);
        redirectAttributes.addFlashAttribute("mensagem", "Assunto cadastrado com sucesso!");
        // redirecionou para a página
        model.setViewName("redirect:/assuntos");
        return model;
    }

    // Lista todos os assuntos
    @GetMapping
    public ModelAndView listAll(ModelAndView model) {
        model.addObject("assuntos", assuntoService.findAll());
        model.setViewName("assuntos/list"); // view: src/main/resources/templates/assuntos/list.html
        return model; 
    }

    // Editar assunto existente
    @GetMapping("/{id}")
    public ModelAndView editAssunto(@PathVariable("id") Integer id, ModelAndView model) {
        model.addObject("assunto", assuntoService.findById(id));
        model.setViewName("assuntos/form");
        return model;
    }

    @GetMapping("/delete/{id}")
    public ModelAndView deleteAssunto(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes, ModelAndView model) {
        assuntoService.deleteById(id);
        redirectAttributes.addFlashAttribute("mensagem", "Assunto removido com sucesso!");
        model.setViewName("redirect:/assuntos");
        return model;
}
}
