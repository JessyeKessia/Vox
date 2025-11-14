package br.edu.ifpb.pweb2.vox.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import br.edu.ifpb.pweb2.vox.model.Processo;
import br.edu.ifpb.pweb2.vox.model.Assunto;
import java.util.List;
import br.edu.ifpb.pweb2.vox.service.AssuntoService;
import br.edu.ifpb.pweb2.vox.service.ProcessoService;

@Controller
@RequestMapping("/processos")
public class ProcessoController {

    @Autowired
    private ProcessoService processoService;

    @Autowired
    private AssuntoService assuntoService;

    // atribuindo a lista de assuntos ao modelo para todas as requisições do controlador
    // terem na chamada a lista de asssuntos disponiveis para fazer o cadastro de processos
    @ModelAttribute("assuntosItens")
    public List<Assunto> getAssuntos() {
        return assuntoService.findAll();
    }

    @GetMapping("/form")
    public ModelAndView getForm(Processo processo, ModelAndView model) {
        model.setViewName("processos/form");
        model.addObject("processo", processo);
        return model;
    }

    @PostMapping
    public ModelAndView addProcesso(Processo processo, ModelAndView modelAndView) {
        processoService.save(processo);
        modelAndView.setViewName("processos/list");
        modelAndView.addObject("processos", processoService.findAll());
        return modelAndView;
    }
 
    @GetMapping
    public ModelAndView list(ModelAndView modelAndView){
        modelAndView.setViewName("processos/list");
        modelAndView.addObject("processos", processoService.findAll());
        return modelAndView;
    }
    
    @GetMapping("/{id}")
    public ModelAndView getContaById(@PathVariable(value = "id") Integer id, ModelAndView model) {
        model.addObject("processo", processoService.findById(id));
        model.setViewName("processos/form");
        return model;
    }
}
