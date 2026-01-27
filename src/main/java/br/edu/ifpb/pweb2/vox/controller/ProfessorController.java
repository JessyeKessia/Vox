package br.edu.ifpb.pweb2.vox.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import br.edu.ifpb.pweb2.vox.entity.Processo;
import br.edu.ifpb.pweb2.vox.entity.Usuario;
import br.edu.ifpb.pweb2.vox.enums.StatusProcesso;
import br.edu.ifpb.pweb2.vox.enums.TipoDecisao;
import br.edu.ifpb.pweb2.vox.service.ProcessoService;
import br.edu.ifpb.pweb2.vox.service.VotoService;

@Controller
@RequestMapping("/professores")
public class ProfessorController {

    @Autowired
    private ProcessoService processoService;

    @Autowired
    private VotoService votoService;

    @ModelAttribute("decisaoItens")
    public TipoDecisao[] getDecisao() {
        return TipoDecisao.values();
    }

    @GetMapping
    public ModelAndView listarProcessos(@AuthenticationPrincipal Usuario usuarioLogado) {
        List<Processo> processos = processoService.findByProfessor(usuarioLogado);
        ModelAndView modelAndView = new ModelAndView("processos/professores/list");
        modelAndView.addObject("processos", processos);
        return modelAndView;
    
    }

    @GetMapping("/decisao/form/{id}")
    public ModelAndView cadastrarDecisao(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("processos/professores/form");
        modelAndView.addObject("processo", processoService.findById(id));
        return modelAndView;
    }


    @PostMapping("/decisao/{id}")
    public ModelAndView salvarDecisao(@PathVariable Long id,  @ModelAttribute Processo processo,
        BindingResult result,
        @AuthenticationPrincipal Usuario usuarioLogado, ModelAndView model,
        RedirectAttributes redirectAttributes) {
        
        Processo processoExistente = processoService.findById(id);
        
        if (processoExistente == null) {
            redirectAttributes.addFlashAttribute("erro", 
            "Processo não encontrado!");
            model.setViewName("redirect:/professores");
            return model;
        }
        if (result.hasErrors()) {

        }
        
        processoExistente.setParecerTexto(processo.getParecerTexto().trim());
        processoExistente.setDecisaoRelator(TipoDecisao.valueOf(processo.getDecisaoRelator().name()));
        processoExistente.setDataParecer(LocalDate.now());
        processoExistente.setStatus(StatusProcesso.DISPONIVEL);
        processoService.save(processoExistente);
        redirectAttributes.addFlashAttribute("sucesso", "Decisão registrado com sucesso!");
        model.setViewName("redirect:/professores");
        
        return model;
    }
}
