/* package br.edu.ifpb.pweb2.vox.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
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
import br.edu.ifpb.pweb2.vox.entity.Professor;
import br.edu.ifpb.pweb2.vox.entity.Usuario;
import br.edu.ifpb.pweb2.vox.entity.Aluno;
import br.edu.ifpb.pweb2.vox.enums.StatusProcesso;
import br.edu.ifpb.pweb2.vox.repository.AlunoRepository;
import br.edu.ifpb.pweb2.vox.service.AssuntoService;
import br.edu.ifpb.pweb2.vox.service.ProcessoService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

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
    public ModelAndView addProcesso(@Valid Processo processo, BindingResult bindingResult, ModelAndView modelAndView, HttpSession session) {
        
        // valida Assunto enviado evitar problemas no Hibernate TransientPropertyValueException
        if (processo.getAssunto() == null || processo.getAssunto().getId() == null) {
            bindingResult.rejectValue("assunto", "assunto.empty", "Selecione um assunto");
        } else {
            var assunto = assuntoService.findById(processo.getAssunto().getId());
            if (assunto.isPresent()) {
                processo.setAssunto(assunto); 
            } else {
                bindingResult.rejectValue("assunto", "assunto.notfound", "Assunto inválido");
            }
        }

        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("processos/form");
            modelAndView.addObject("processo", processo);
            return modelAndView;
        }

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
        @RequestParam(required = false, defaultValue = "desc") String ordenar, 
        HttpSession session) 
        {
            ModelAndView mv = new ModelAndView("processos/list");

            // pega o usuário da sessão (pode ser null, não redireciona)
            Usuario usuario = (Usuario) session.getAttribute("usuario");

            Long alunoId = (usuario != null) ? usuario.getId() : null;

            // converte status
            StatusProcesso statusEnum = null;
            if (status != null && !status.isBlank()) {
                try {
                    statusEnum = StatusProcesso.valueOf(status);
                } catch (Exception ignored) {}
            }

            // assunto
            String assuntoFiltro = (assunto != null && !assunto.isBlank()) ? assunto : null;

            // chama o serviço passando o id do aluno (mesmo se null)
            List<Processo> processos = processoService.findForAlunoProcessos(statusEnum, assuntoFiltro, alunoId);

            // devolve filtros e lista
            mv.addObject("processos", processos);
            mv.addObject("statusSelecionado", status);
            mv.addObject("assuntoSelecionado", assunto);

            return mv;
        }
    // lista os processos de designados a cada professor
    @GetMapping("/professores")
    public ModelAndView listarProcessos(HttpSession session) {
        
        // pega o professor logado da sessão
        Professor professorLogado = (Professor) session.getAttribute("usuario"); 

        List<Processo> processos = processoService.findByProfessor(professorLogado);

        ModelAndView modelAndView = new ModelAndView("professores/processos/list");
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
 */