package br.edu.ifpb.pweb2.vox.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
        assuntoService.save(assunto);
        redirectAttributes.addFlashAttribute("mensagemSucesso", "Assunto cadastrado com sucesso!");
        // PRG: Redirecionou para a página
        model.setViewName("redirect:/assuntos");
        return model;
    }

    // Lista todos os assuntos
    @GetMapping
    public ModelAndView listAll(
            @PageableDefault(size = 10, sort = { "nome" }, direction = Sort.Direction.ASC) Pageable pageable,
            ModelAndView model) {
        // MUDANÇA: Usando o método paginado e adicionando o objeto Page
        model.addObject("assuntosPaginados", assuntoService.findAll(pageable));
        model.setViewName("assuntos/list"); // view: src/main/resources/templates/assuntos/list.html
        return model;
    }

    // Editar assunto existente
    @GetMapping("/{id}")
    public ModelAndView editAssunto(@PathVariable("id") Long id, ModelAndView model) {
        model.addObject("assunto", assuntoService.findById(id));
        model.setViewName("assuntos/form");
        return model;
    }

    @GetMapping("/delete/{id}")
    public ModelAndView deleteAssunto(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes,
            ModelAndView model) {
        try {
            assuntoService.deleteById(id);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Assunto removido com sucesso!");
        } catch (RuntimeException e) {
            // REQNAOFUNC 5: Tratamento de erro básico
            redirectAttributes.addFlashAttribute("mensagemErro",
                    "Não foi possível remover o assunto. Verifique se ele está sendo usado em algum processo.");
        }
        // PRG: Redirecionou para a página
        model.setViewName("redirect:/assuntos");
        return model;
    }
}