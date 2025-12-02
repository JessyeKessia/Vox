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
import br.edu.ifpb.pweb2.vox.entity.Usuario;
import br.edu.ifpb.pweb2.vox.entity.Aluno;
import br.edu.ifpb.pweb2.vox.enums.StatusProcesso;
import br.edu.ifpb.pweb2.vox.repository.AlunoRepository;
import br.edu.ifpb.pweb2.vox.service.AssuntoService;
import br.edu.ifpb.pweb2.vox.service.ProcessoService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/processos")
public class ProcessoController {

    @Autowired
    private ProcessoService processoService;

    @Autowired
    private AssuntoService assuntoService;

    @Autowired
    private AlunoRepository alunoRepository;

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
    public ModelAndView addProcesso(Processo processo, ModelAndView modelAndView, HttpSession session) {
        // pega o usuário logado da sessão
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        // procura se o aluno existe no repositorio
        Aluno aluno = alunoRepository.findById(usuario.getId())
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado."));

        // associa o processo ao aluno interessado
        processo.setAlunoInteressado(aluno);

        processoService.save(processo);

        modelAndView.setViewName("redirect:/processos");
        return modelAndView;
    }

    // lista os processos com filtros
    @GetMapping()
    public ModelAndView list(
        @RequestParam(required = false) String status,
        @RequestParam(required = false) String assunto,
        @RequestParam(required = false, defaultValue = "false") boolean ordenarPorData) 
        {
        
        // seta o caminho da view
        ModelAndView modelAndView = new ModelAndView("processos/list");

        // elementos marcados pelo usuário sendo mandandos para a view
        modelAndView.addObject("statusSelecionado", status);
        modelAndView.addObject("assuntoSelecionado", assunto);

        // FILTROS
        // devolve filtros para a view
        modelAndView.addObject("statusSelecionado", status);
        modelAndView.addObject("assuntoSelecionado", assunto);
        modelAndView.addObject("ordenarPorData", ordenarPorData);

        // converte o status recebido para Enum (se tiver)
        StatusProcesso statusEnum = null;
        if (status != null && !status.isBlank()) {
            try {
                statusEnum = StatusProcesso.valueOf(status);
            } catch (Exception e) {
                statusEnum = null; // evita quebrar a aplicação
            }
        }

        // prepara assunto
        String assuntoFiltro = (assunto != null && !assunto.isBlank()) ? assunto : null;

        // chamada ao service
        List<Processo> processos = processoService.findForAlunoProcessos(
                statusEnum,
                assuntoFiltro,
                ordenarPorData
        );

        modelAndView.addObject("processos", processos);
        return modelAndView;
            
    }
    
    @GetMapping("/{id}")
    public ModelAndView getProcessoById(@PathVariable(value = "id") Long id, ModelAndView model) {
        model.addObject("processo", processoService.findById(id));
        model.setViewName("processos/form");
        return model;
    }
}
