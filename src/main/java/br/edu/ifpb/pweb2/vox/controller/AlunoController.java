package br.edu.ifpb.pweb2.vox.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

import br.edu.ifpb.pweb2.vox.entity.Assunto;
import br.edu.ifpb.pweb2.vox.entity.Processo;
import br.edu.ifpb.pweb2.vox.entity.Usuario;
import br.edu.ifpb.pweb2.vox.enums.Role;
import br.edu.ifpb.pweb2.vox.enums.StatusProcesso;
import br.edu.ifpb.pweb2.vox.service.AssuntoService;
import br.edu.ifpb.pweb2.vox.service.ProcessoService;
import br.edu.ifpb.pweb2.vox.service.UsuarioService;
import jakarta.validation.Valid;


@Controller
@RequestMapping("/processos")
public class AlunoController {

    @Autowired
    private ProcessoService processoService;

    @Autowired
    private AssuntoService assuntoService;

    @Autowired
    private UsuarioService usuarioService;

    // atribuindo a lista de assuntos ao modelo para todas as requisições do controlador
    // terem na chamada a lista de asssuntos disponiveis para fazer o cadastro de processos
    @ModelAttribute("assuntosItens")
    public List<Assunto> getAssuntos() {
        return assuntoService.findAll();
    }

    // manda a lista de alunos para o modelo
    @ModelAttribute("alunosItens")
    public List<Usuario> getAlunos() {
        return usuarioService.findByRole(Role.ALUNO);
    }

    // Atribuindo a lista de status ao modelo
    //  para todas o filtro ter acesso aos status
    @ModelAttribute("statusItens")
    public StatusProcesso[] getStatusList() {
        return StatusProcesso.values();
    }

    // pega o formulário de cadastro de processo assim que voce acessa a rota
    @GetMapping("/form")
    public ModelAndView CadastrarProcesso(Processo processo, ModelAndView model) {
        model.setViewName("processos/form");
        model.addObject("processo", processo);
        return model;
    }

    // salva o processando quando você clica em salvar no formulário
    @PostMapping
    public ModelAndView salvarProcesso(
            @Valid @ModelAttribute Processo processo,
            BindingResult bindingResult,
            ModelAndView modelAndView,
            @AuthenticationPrincipal Usuario usuario) {

        // erros de validação dos campos normais
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("processos/form");
            return modelAndView;
        }

        // seta o aluno logado como autor do processo
        processo.setAlunoInteressado(usuario);

        MultipartFile arquivo = processo.getRequerimentoArquivo();

        // se o usuário enviou um arquivo
        if (arquivo != null && !arquivo.isEmpty()) {

            // valida tipo
            if (!"application/pdf".equalsIgnoreCase(arquivo.getContentType())) {
                bindingResult.rejectValue(
                    "requerimentoArquivo",
                    "arquivo.invalido",
                    "O arquivo deve ser um PDF."
                );
                modelAndView.setViewName("processos/form");
                return modelAndView;
            }

            try {
                // converte MultipartFile -> byte[]
                processo.setRequerimentoPdf(arquivo.getBytes());
            } catch (IOException e) {
                bindingResult.rejectValue(
                    "requerimentoArquivo",
                    "arquivo.erro",
                    "Erro ao processar o arquivo."
                );
                modelAndView.setViewName("processos/form");
                return modelAndView;
            }
        }

        processoService.save(processo);
        return new ModelAndView("redirect:/processos");
    }


    // lista os processos com filtros
    @GetMapping()
    public ModelAndView listarProcessos(
        @RequestParam(required = false) String status,
        @RequestParam(required = false) String assunto, 
        @AuthenticationPrincipal Usuario usuarioLogado) 
        {
            ModelAndView mv = new ModelAndView("processos/list");

            Long alunoId = usuarioLogado.getId(); // sempre existe aqui

            StatusProcesso statusEnum = null;
            if (status != null && !status.isBlank()) {
                try {
                    statusEnum = StatusProcesso.valueOf(status);
                } catch (IllegalArgumentException ignored) {}
            }

            String assuntoFiltro = (assunto != null && !assunto.isBlank()) ? assunto : null;

            List<Processo> processos =
                    processoService.findForAlunoProcessos(statusEnum, assuntoFiltro, alunoId);

            // devolve filtros e lista
            mv.addObject("processos", processos);
            mv.addObject("statusSelecionado", status);
            mv.addObject("assuntoSelecionado", assunto);

            return mv;
        }

    @GetMapping("/{id}/requerimento")
    public ResponseEntity<byte[]> visualizarRequerimento(@PathVariable Long id) {
        Processo processo = processoService.findById(id);

        if (processo.getRequerimentoPdf() == null || processo.getRequerimentoPdf().length == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("inline", "requerimento-" + processo.getNumero() + ".pdf");

        return new ResponseEntity<>(processo.getRequerimentoPdf(), headers, HttpStatus.OK);
    }


    @GetMapping("/delete/{id}")
    public ModelAndView excluirProcesso(@PathVariable Long id) {
        processoService.deleteById(id);
        return new ModelAndView("redirect:/processos");
    }
    
    @GetMapping("/edit/{id}")
    public ModelAndView getProcessoById(@PathVariable Long id, ModelAndView model, RedirectAttributes redirectAttributes) {
        Processo processo = processoService.findById(id);

        if (processo.getStatus() != StatusProcesso.CRIADO ) {
            redirectAttributes.addFlashAttribute(
            "erro",
            "Este processo não pode mais ser editado pois já foi distribuído."
        );
        return new ModelAndView("redirect:/processos");
    }

    ModelAndView mv = new ModelAndView("processos/form");
    mv.addObject("processo", processo);
    return mv;
}
}
