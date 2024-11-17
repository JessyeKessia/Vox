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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;

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

    // atribuindo a lista de assuntos ao modelo para todas as requisições do
    // controlador
    // terem na chamada a lista de asssuntos disponiveis para fazer o cadastro de
    // processos
    @ModelAttribute("assuntosItens")
    public List<Assunto> getAssuntos() {
        return assuntoService.findAll();
    }

    // Atribuindo a lista de status ao modelo
    // para todas o filtro ter acesso aos status
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
    public ModelAndView addProcesso(Processo processo, RedirectAttributes redirectAttributes,
            ModelAndView modelAndView) {
        processoService.save(processo);
        redirectAttributes.addFlashAttribute("mensagemSucesso", "Processo cadastrado com sucesso!");
        // PRG: redireciona para a lista de processos após salvar
        modelAndView.setViewName("redirect:/processos");
        return modelAndView;
    }

    // ta vazio pq usa a rota "/processos"
    @GetMapping()
    public ModelAndView list(
            // MUDANÇA: Adiciona Pageable com ordenação default (REQNAOFUNC 9)
            @PageableDefault(size = 10, sort = { "dataRecepcao" }, direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String assunto) {

        // seta o caminho da view
        ModelAndView modelAndView = new ModelAndView("processos/list");

        // elementos marcados pelo usuário sendo mandandos para a view
        modelAndView.addObject("statusSelecionado", status);
        modelAndView.addObject("assuntoSelecionado", assunto);

        Page<Processo> processosPaginados; // MUDANÇA: Objeto paginado

        // FILTROS (adaptados para usar métodos paginados)
        if (status != null && !status.isEmpty() && assunto != null && !assunto.isEmpty()) {

            // Para simplificar, priorizamos o filtro por status com paginação
            processosPaginados = processoService.findByStatus(
                    StatusProcesso.valueOf(status), pageable);

        } else if (status != null && !status.isEmpty()) {

            processosPaginados = processoService.findByStatus(
                    StatusProcesso.valueOf(status), pageable);

        } else if (assunto != null && !assunto.isEmpty()) {

            processosPaginados = processoService.findByAssunto(assunto, pageable);

        } else {
            // MUDANÇA: Se não houver filtro, usa o findAll paginado (a ordenação é feita
            // pelo @PageableDefault)
            processosPaginados = processoService.findAll(pageable);
        }

        // MUDANÇA: Adiciona o objeto Page (com metadados de paginação) à view
        modelAndView.addObject("processosPaginados", processosPaginados);

        return modelAndView;

    }

    @GetMapping("/{id}")
    public ModelAndView getProcessoById(@PathVariable(value = "id") Integer id, ModelAndView model) {
        model.addObject("processo", processoService.findById(id));
        model.setViewName("processos/form");
        return model;
    }
}