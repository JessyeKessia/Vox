package br.edu.ifpb.pweb2.vox.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
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

import br.edu.ifpb.pweb2.vox.entity.Colegiado;
import br.edu.ifpb.pweb2.vox.entity.Processo;
import br.edu.ifpb.pweb2.vox.entity.Reuniao;
import br.edu.ifpb.pweb2.vox.entity.Usuario;
import br.edu.ifpb.pweb2.vox.entity.Voto;
import br.edu.ifpb.pweb2.vox.service.ColegiadoService;
import br.edu.ifpb.pweb2.vox.service.ProcessoService;
import br.edu.ifpb.pweb2.vox.service.ReuniaoService;
import br.edu.ifpb.pweb2.vox.service.UsuarioService;
import br.edu.ifpb.pweb2.vox.service.VotoService;
import groovy.lang.Binding;
import br.edu.ifpb.pweb2.vox.enums.Role;
import br.edu.ifpb.pweb2.vox.enums.StatusProcesso;
import br.edu.ifpb.pweb2.vox.enums.TipoVoto;

@Controller
@RequestMapping("/coordenadores")
public class CoordenadorController {

    @Autowired
    private ProcessoService processoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ColegiadoService colegiadoService;

    @Autowired
    private ReuniaoService reuniaoService;

    @Autowired
    private VotoService votoService;
    
    // manda a lista de professores que não são coordenadores para o modelo
    @ModelAttribute("professoresItens")
    public List<Usuario> getProfessores() {
        return usuarioService.findByRole(Role.PROFESSOR);
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

    /* =========================
       PROCESSOS
       ========================= */

    // pega o formulário de distribuição de processo
    @GetMapping("/distribuir/{id}")
    public ModelAndView cadastrarProcessoDistribuido(@PathVariable Long id, @RequestParam(required = false) Long colegiadoId) {

        ModelAndView modelAndView = new ModelAndView("processos/coordenadores/form");
        // pega o processo selecionado
        modelAndView.addObject("processo", processoService.findById(id));
        // pega todos os colegiados
        modelAndView.addObject("colegiados", colegiadoService.findAll());

        List<Usuario> professoresColegiado =
            (colegiadoId == null)
                ? List.of()
                : colegiadoService.findMembrosByColegiadoId(colegiadoId);

        modelAndView.addObject("professoresColegiado", professoresColegiado);
        modelAndView.addObject("colegiadoSelecionadoId", colegiadoId);
        return modelAndView;
    }

    @PostMapping("/distribuir/{id}")
    public ModelAndView salvarProcessoDistribuido(@PathVariable Long id,  @RequestParam Long colegiadoId, @RequestParam Long professorId, RedirectAttributes redirectAttributes ) {

        // sei qual é o processo
        Processo processo = processoService.findById(id);

        // se o processo for diferente de criado
        if (processo.getStatus() != StatusProcesso.CRIADO) {
        redirectAttributes.addFlashAttribute(
            "erro",
            "Este processo já foi distribuído ou não pode mais ser distribuído."
        );
        return new ModelAndView("redirect:/coordenadores/processos");
        }

        // 4️⃣ Garante que o professor escolhido pertence ao colegiado
        
        Usuario relator = usuarioService.findById(professorId);
        processo.setRelator(relator);
        processo.setDataDistribuicao(LocalDate.now());
        processo.setStatus(StatusProcesso.DISTRIBUIDO);

        processoService.save(processo);

        redirectAttributes.addFlashAttribute("mensagem", "Processo distribuído com sucesso!");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/coordenadores");
        return modelAndView;   
    }

    @GetMapping
    public ModelAndView listarProcessosDistribuidos( 
        @RequestParam(required = false) StatusProcesso status, 
        @RequestParam(required = false) Long alunoInteressadoId, 
        @RequestParam(required = false) Long relatorId) {

        ModelAndView modelAndView = new ModelAndView("processos/coordenadores/list");
        
        // mantém o que o usuário escolheu nos selects
        modelAndView.addObject("statusSelecionado", status);
        modelAndView.addObject("alunoSelecionado", alunoInteressadoId);
        modelAndView.addObject("professorSelecionado", relatorId);

        // busca usando service (lógica de filtragem fica lá)
        List<Processo> processos = processoService.findForCoordenadorProcessos(status, alunoInteressadoId, relatorId);
        modelAndView.addObject("processos", processos);

        return modelAndView;
    }

    /* =========================
       REUNIÕES
       ========================= */

    @ModelAttribute("contagemVotos")
    public Map<Long, Map<String, Long>> contagemVotos() {

        Map<Long, Map<String, Long>> resultado = new HashMap<>();

        List<Voto> votos = votoService.findAll();

        // 1️⃣ Conta votos
        for (Voto v : votos) {

            Long processoId = v.getProcesso().getId();

            resultado.putIfAbsent(processoId, new HashMap<>());

            Map<String, Long> contagem = resultado.get(processoId);

            contagem.putIfAbsent("COM_RELATOR", 0L);
            contagem.putIfAbsent("DIVERGENTE", 0L);
            contagem.putIfAbsent("AUSENTE", 0L);

            if (v.getTipoVoto() == TipoVoto.COM_RELATOR) {
                contagem.put("COM_RELATOR", contagem.get("COM_RELATOR") + 1);
            } 
            else if (v.getTipoVoto() == TipoVoto.DIVERGENTE) {
                contagem.put("DIVERGENTE", contagem.get("DIVERGENTE") + 1);
            }
        }

        // 2️⃣ Calcula AUSENTES
        for (Map.Entry<Long, Map<String, Long>> entry : resultado.entrySet()) {

            Long processoId = entry.getKey();
            Map<String, Long> contagem = entry.getValue();

            // pega qualquer voto do processo para acessar a reunião
            Voto voto = votos.stream()
                    .filter(v -> v.getProcesso().getId().equals(processoId))
                    .findFirst()
                    .orElse(null);

            if (voto != null) {
                Reuniao reuniao = voto.getReuniao();

                long totalPresentes = reuniao.getMembrosPresentes().size();
                long totalVotos = contagem.get("COM_RELATOR") + contagem.get("DIVERGENTE");

                contagem.put("AUSENTE", totalPresentes - totalVotos);
            }
        }

        return resultado;
    }

@ModelAttribute("votosPorProcesso")
    public Map<Long, List<Voto>> votosPorProcesso() {
        return votoService.findAll()
                .stream()
                .collect(Collectors.groupingBy(v -> v.getProcesso().getId()));
    }

    @GetMapping("/reunioes")
    public ModelAndView listarReuniao() {
        ModelAndView mv = new ModelAndView("reunioes/coordenadores/list");
        mv.addObject("reunioes", reuniaoService.findAll());
        boolean existeReuniaoEmAndamento = reuniaoService.existeReuniaoEmAndamento();

        mv.addObject("existeReuniaoEmAndamento", existeReuniaoEmAndamento);

        return mv;
    }

    @GetMapping("/reunioes/form")
    public ModelAndView listarPautasParaReuniao(@RequestParam(required = false) Long colegiadoId, ModelAndView mv) {
        mv.setViewName("reunioes/coordenadores/form");

        mv.addObject("colegiados", colegiadoService.findAll());
        mv.addObject("colegiadoSelecionadoId", colegiadoId);

        // Lista membros do colegiado selecionado
        List<Usuario> membros = (colegiadoId != null) 
            ? colegiadoService.findMembrosByColegiadoId(colegiadoId) 
            : List.of();
        mv.addObject("membros", membros);

        // Lista processos DISPONÍVEIS para os membros do colegiado selecionado
        List<Processo> processosItens = (colegiadoId != null) 
            ? processoService.findForCoordenadorProcessos(StatusProcesso.DISPONIVEL, null, null).stream()
                .filter(p -> membros.contains(p.getRelator()))
                .toList()
            : List.of();
        mv.addObject("processosItens", processosItens);

        mv.addObject("membrosSelecionados", List.of());   // lista vazia
        mv.addObject("processosSelecionados", List.of()); // lista vazia

         // Inicializa objeto reuniao
        mv.addObject("reuniao", new Reuniao());

        return mv;
    }
 
    @PostMapping("/reunioes/save")
    public ModelAndView salvarReuniao(@Valid Reuniao reuniao, 
        BindingResult bindingResult,
        @RequestParam Long colegiadoId,
        @RequestParam(required = false, name = "processosSelecionados") List<Long> pautaIds,
        @RequestParam(required = false, name = "membrosSelecionados") List<Long> membrosIds
    ) {
        // Verifica se há erros de validação
        if (bindingResult.hasErrors()) {
            ModelAndView mv = new ModelAndView("reunioes/coordenadores/form");

            // Repovoar os dados necessários no modelo
            mv.addObject("colegiados", colegiadoService.findAll());

            return mv; // Retorna o formulário com os erros
        }

        // Popula os membros
        Set<Usuario> membrosPresentes = new HashSet<>(usuarioService.findUsuariosByIdIn(membrosIds));
        reuniao.setMembrosPresentes(membrosPresentes);

        // Buscar o Colegiado do banco
        Colegiado colegiado = colegiadoService.findById(colegiadoId);
        reuniao.setColegiado(colegiado);

        // Popula a pauta
        List<Processo> pauta = processoService.findAllByIds(pautaIds);
        reuniao.setPauta(pauta);

        reuniaoService.save(reuniao);
        ModelAndView mv = new ModelAndView("redirect:/coordenadores/reunioes");

        return mv;
    }


    @GetMapping("reunioes/conduzir/{id}")
    public ModelAndView conduzirReuniao(@PathVariable Long id) {
        ModelAndView mv = new ModelAndView("reunioes/coordenadores/conduzir"); 
        mv.addObject("reuniao", reuniaoService.findById(id));
        return mv;
    }

   @PostMapping("/reunioes/colocar-em-pauta/{processoId}")
    public String colocarEmPauta(
            @PathVariable Long processoId,
            @RequestParam Long reuniaoId,
            RedirectAttributes redirectAttributes) {

        Processo processo = processoService.findById(processoId);

        if (processo == null) {
            redirectAttributes.addFlashAttribute("erro", "Processo não encontrado.");
            return "redirect:/coordenadores/reunioes";
        }

        if (processo.getStatus() != StatusProcesso.DISPONIVEL) {
            redirectAttributes.addFlashAttribute(
                "erro",
                "O processo não está disponível para ser colocado em pauta."
            );
            return "redirect:/coordenadores/reunioes/conduzir/" + reuniaoId;
        }

        processo.setStatus(StatusProcesso.EM_PAUTA);
        processoService.save(processo);

        redirectAttributes.addFlashAttribute(
            "mensagem",
            "Processo colocado em pauta com sucesso."
        );

        // 🔁 recarrega a tela da reunião
        return "redirect:/coordenadores/reunioes/conduzir/" + reuniaoId;
    }



    @PostMapping("reunioes/iniciar/{id}")
    public String iniciarReuniao(@PathVariable Long id) {
        reuniaoService.iniciarSessao(id);
        return "redirect:/coordenadores/reunioes/conduzir/" + id;
    }
    
    @PostMapping("reunioes/finalizar/{id}")
    public String finalizarReuniao(@PathVariable Long id) {
        reuniaoService.finalizarSessao(id);
        return "redirect:/coordenadores/reunioes";
    }

    @PostMapping("/processos/{id}/em-pauta")
    public String colocarEmPauta(@PathVariable Long id) {
        // Buscar o processo pelo ID
        Processo processo = processoService.findById(id);

        // Alterar o status do processo para EM_PAUTA
        processo.setStatus(StatusProcesso.EM_PAUTA);

        // Salvar as alterações no processo
        processoService.save(processo);

        // Redirecionar para a lista de processos
        return "redirect:/processos";
    }
}
