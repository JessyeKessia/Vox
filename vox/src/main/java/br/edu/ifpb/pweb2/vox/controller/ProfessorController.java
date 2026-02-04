package br.edu.ifpb.pweb2.vox.controller;

import java.io.ObjectInputFilter.Status;
import java.security.Principal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifpb.pweb2.vox.entity.Colegiado;
import br.edu.ifpb.pweb2.vox.entity.Processo;
import br.edu.ifpb.pweb2.vox.entity.Reuniao;
import br.edu.ifpb.pweb2.vox.entity.Usuario;
import br.edu.ifpb.pweb2.vox.entity.Voto;
import br.edu.ifpb.pweb2.vox.enums.StatusProcesso;
import br.edu.ifpb.pweb2.vox.enums.StatusReuniao;
import br.edu.ifpb.pweb2.vox.enums.TipoDecisao;
import br.edu.ifpb.pweb2.vox.enums.TipoVoto;
import br.edu.ifpb.pweb2.vox.service.ColegiadoService;
import br.edu.ifpb.pweb2.vox.service.ProcessoService;
import br.edu.ifpb.pweb2.vox.service.ReuniaoService;
import br.edu.ifpb.pweb2.vox.service.UsuarioService;
import br.edu.ifpb.pweb2.vox.service.VotoService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/professores")
public class ProfessorController {

    @Autowired
    private ProcessoService processoService;

    @Autowired
    private VotoService votoService;

    @Autowired
    private ReuniaoService reuniaoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ColegiadoService colegiadoService;

     // atribuindo a lista de decisões ao modelo para todas as requisições do controlador

    @ModelAttribute("decisaoItens")
    public TipoDecisao[] getDecisao() {
        return TipoDecisao.values();
    }

    @ModelAttribute("tipoVotoItens")
    public TipoVoto[] getTipoVoto() {
        return TipoVoto.values();
    }
    @ModelAttribute("colegiadosItens")
    public List<Colegiado> getColegiados() {
        return colegiadoService.findAll();
    }
    @ModelAttribute("statusReuniaoItens")
    public StatusReuniao[] getStatusReuniao() {
        return StatusReuniao.values();
    }

    /* =========================
       PROCESSOS
       ========================= */


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

    /* =========================
       REUNIÕES
       ========================= */

    @GetMapping("/reunioes")
    public ModelAndView listarReuniao(
            @AuthenticationPrincipal Usuario usuarioLogado,
            @RequestParam(required = false) Long colegiado,
            @RequestParam(required = false) String status) {

        ModelAndView mv = new ModelAndView("reunioes/professores/list");

        Usuario professor = usuarioService.findById(usuarioLogado.getId());

        // Converter status string para enum
        StatusReuniao statusEnum = (status != null && !status.isEmpty()) ? StatusReuniao.valueOf(status) : null;

        // Buscar reuniões filtradas direto no banco
        List<Reuniao> reunioes = reuniaoService.findFiltros(colegiado, statusEnum);

        // Map para verificar se o professor já votou em todos os processos da reunião
        Map<Long, Boolean> possuiProcessosParaVotarMap = new HashMap<>();

        for (Reuniao r : reunioes) {

            // Verifica se ainda há processos em pauta atribuídos ao professor que não foram votados
            boolean possuiProcessosParaVotar = r.getPauta().stream()
                    .filter(p -> p.getStatus() == StatusProcesso.EM_PAUTA) // processos em pauta
                    .anyMatch(p -> !votoService.jaVotou(professor, p)); // ainda não votou

            possuiProcessosParaVotarMap.put(r.getId(), possuiProcessosParaVotar);
        }

        // Listas para popular os filtros
        List<Colegiado> colegiadosItens = colegiadoService.findAll();
        List<StatusReuniao> statusItens = Arrays.asList(StatusReuniao.values());

        mv.addObject("colegiadosItens", colegiadosItens);
        mv.addObject("statusItens", statusItens);
        mv.addObject("reunioes", reunioes);
        mv.addObject("possuiProcessosParaVotarMap", possuiProcessosParaVotarMap);

        return mv;
    }

   @GetMapping("/reunioes/voto/{id}")
    public ModelAndView votarProcessos(
            @PathVariable Long id,
            @AuthenticationPrincipal Usuario usuarioLogado) {

        Reuniao reuniao = reuniaoService.findById(id);
        Usuario professor = usuarioService.findById(usuarioLogado.getId());

        ModelAndView mv = new ModelAndView("reunioes/professores/form");

        mv.addObject("reuniao", reuniao);

        // Processos EM PAUTA direcionados ao professor
        List<Processo> processos = reuniao.getPauta().stream()
            .filter(p -> p.getStatus() == StatusProcesso.EM_PAUTA)
            .filter(p -> reuniao.getMembrosPresentes().contains(professor))
            .toList();

        mv.addObject("processos", processos);

        // Votos já feitos pelo professor na reunião
        Map<Long, Voto> votoMap =
            votoService.buscarVotosDoProfessorNaReuniao(professor, reuniao);

        mv.addObject("votoMap", votoMap);
        mv.addObject("tipoVotoItens", TipoVoto.values());

        // todos processos votados?
        boolean todosProcessosVotados =
            !processos.isEmpty() &&
            processos.stream().allMatch(p -> votoMap.containsKey(p.getId()));

        mv.addObject("todosProcessosVotados", todosProcessosVotados);

        return mv;
    }


    @PostMapping("/reunioes/voto/{reuniaoId}/{processoId}")
    public String votarProcesso(
            @PathVariable Long reuniaoId,
            @PathVariable Long processoId,
            @RequestParam TipoVoto tipoVoto,
            @AuthenticationPrincipal Usuario usuarioLogado,
            RedirectAttributes redirectAttributes) {

        Usuario professor = usuarioService.findById(usuarioLogado.getId());
        Reuniao reuniao = reuniaoService.findById(reuniaoId);
        Processo processo = processoService.findById(processoId);

        // 🔒 Já votou neste processo?
        if (votoService.jaVotouNoProcesso(professor, reuniao, processo)) {
            redirectAttributes.addFlashAttribute(
                "erro", "Você já votou neste processo."
            );
            return "redirect:/professores/reunioes/voto/" + reuniaoId;
        }

        Voto voto = new Voto();
        voto.setProfessor(professor);
        voto.setReuniao(reuniao);
        voto.setProcesso(processo);
        voto.setTipoVoto(tipoVoto);

        votoService.save(voto);

        redirectAttributes.addFlashAttribute(
            "sucesso", "Voto registrado com sucesso!"
        );

        // 🔁 RECARREGA A PÁGINA
        return "redirect:/professores/reunioes/voto/" + reuniaoId;
    }


    
}