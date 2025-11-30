package br.edu.ifpb.pweb2.vox.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import br.edu.ifpb.pweb2.vox.entity.Assunto;
import br.edu.ifpb.pweb2.vox.entity.Processo;
import br.edu.ifpb.pweb2.vox.service.AssuntoService;
import br.edu.ifpb.pweb2.vox.service.ProcessoService;
import br.edu.ifpb.pweb2.vox.types.StatusProcesso;

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

    // Atribuindo a lista de status ao modelo
    //  para todas o filtro ter acesso aos status
    @ModelAttribute("statusItens")
    public StatusProcesso[] getStatusList() {
        return StatusProcesso.values();
    }

    // pega o formulário de cadastro de processo assim que voce acessa a rota
    @GetMapping("/form")
    public ModelAndView getForm(Processo processo, ModelAndView model) {
        model.setViewName("processos/form");
        model.addObject("processo", processo);
        return model;
    }

    // salva o processando quando você clica em salvar no formulário
    @PostMapping
    public ModelAndView addProcesso(Processo processo, ModelAndView modelAndView) {
        processoService.save(processo);
        // redireciona para a lista de processos após salvar
        modelAndView.setViewName("redirect:/processos");
        modelAndView.addObject("processos", processoService.findAll());
        return modelAndView;
    }

    // ta vazio pq usa a rota "/processos"
    @GetMapping()
    public ModelAndView list(
        @RequestParam(required = false) String status, 
        @RequestParam(required = false) String assunto) {
        
        // seta o caminho da view
        ModelAndView modelAndView = new ModelAndView("processos/list");

        // elementos marcados pelo usuário sendo mandandos para a view
        modelAndView.addObject("statusSelecionado", status);
        modelAndView.addObject("assuntoSelecionado", assunto);

        List<Processo> processos;

        // FILTROS
        if (status != null && !status.isEmpty() &&
        assunto != null && !assunto.isEmpty()) {

        processos = processoService.findByAssunto(assunto).stream()
                .filter(p -> p.getStatus() == StatusProcesso.valueOf(status))
                .toList();

        } else if (status != null && !status.isEmpty()) {

            processos = processoService.findByStatus(
                    StatusProcesso.valueOf(status)
            );

        } else if (assunto != null && !assunto.isEmpty()) {

            processos = processoService.findByAssunto(assunto);

        } else {
            processos = processoService.findAllOrderedByCreationDate();
        }

        modelAndView.addObject("processos", processos);
        return modelAndView;
            
    }
    
    @GetMapping("/{id}")
    public ModelAndView getProcessoById(@PathVariable(value = "id") Integer id, ModelAndView model) {
        model.addObject("processo", processoService.findById(id));
        model.setViewName("processos/form");
        return model;
    }
}
