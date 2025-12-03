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
import br.edu.ifpb.pweb2.vox.entity.Professor;
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

<<<<<<< HEAD
    // atribuindo a lista de assuntos ao modelo para todas as requisições do
    // controlador
    // terem na chamada a lista de asssuntos disponiveis para fazer o cadastro de
    // processos
=======
    @Autowired
    private AlunoRepository alunoRepository;

    // atribuindo a lista de assuntos ao modelo para todas as requisições do controlador
    // terem na chamada a lista de asssuntos disponiveis para fazer o cadastro de processos
>>>>>>> e08501738ef912fd79693544bc9c5321da2e4082
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
<<<<<<< HEAD
    public ModelAndView addProcesso(Processo processo, RedirectAttributes redirectAttributes,
            ModelAndView modelAndView) {
        processoService.save(processo);
        redirectAttributes.addFlashAttribute("mensagemSucesso", "Processo cadastrado com sucesso!");
        // PRG: redireciona para a lista de processos após salvar
=======
    public ModelAndView addProcesso(Processo processo, ModelAndView modelAndView, HttpSession session) {
        // pega o usuário logado da sessão
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        // procura se o aluno existe no repositorio
        Aluno aluno = alunoRepository.findById(usuario.getId())
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado."));

        // associa o processo ao aluno interessado
        processo.setAlunoInteressado(aluno);

        processoService.save(processo);

>>>>>>> e08501738ef912fd79693544bc9c5321da2e4082
        modelAndView.setViewName("redirect:/processos");
        return modelAndView;
    }

    // lista os processos com filtros
    @GetMapping()
    public ModelAndView list(
<<<<<<< HEAD
            // MUDANÇA: Adiciona Pageable com ordenação default (REQNAOFUNC 9)
            @PageableDefault(size = 10, sort = { "dataRecepcao" }, direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String assunto) {

=======
        @RequestParam(required = false) String status,
        @RequestParam(required = false) String assunto,
        @RequestParam(required = false, defaultValue = "false") boolean ordenarPorData) 
        {
        
>>>>>>> e08501738ef912fd79693544bc9c5321da2e4082
        // seta o caminho da view
        ModelAndView modelAndView = new ModelAndView("processos/list");

        // elementos marcados pelo usuário sendo mandandos para a view
        modelAndView.addObject("statusSelecionado", status);
        modelAndView.addObject("assuntoSelecionado", assunto);

<<<<<<< HEAD
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

=======
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
>>>>>>> e08501738ef912fd79693544bc9c5321da2e4082
        return modelAndView;

    }
<<<<<<< HEAD

=======
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
    
>>>>>>> e08501738ef912fd79693544bc9c5321da2e4082
    @GetMapping("/{id}")
    public ModelAndView getProcessoById(@PathVariable(value = "id") Long id, ModelAndView model) {
        model.addObject("processo", processoService.findById(id));
        model.setViewName("processos/form");
        return model;
    }
}