package br.edu.ifpb.pweb2.vox.controller;

import java.time.LocalDate;
import java.util.List;

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


import br.edu.ifpb.pweb2.vox.entity.Processo;
import br.edu.ifpb.pweb2.vox.entity.Reuniao;
import br.edu.ifpb.pweb2.vox.entity.Usuario;
import br.edu.ifpb.pweb2.vox.service.ColegiadoService;
import br.edu.ifpb.pweb2.vox.service.ProcessoService;
import br.edu.ifpb.pweb2.vox.service.ReuniaoService;
import br.edu.ifpb.pweb2.vox.service.UsuarioService;
import br.edu.ifpb.pweb2.vox.enums.Role;
import br.edu.ifpb.pweb2.vox.enums.StatusProcesso;

@Controller
@RequestMapping("/coordenadores")
public class CoordenadorController {

    @Autowired
    private ProcessoService processoService;

        
    @Autowired
    private ReuniaoService reuniaoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ColegiadoService colegiadoService;

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

    // pega o formulário de distribuição de processo
    @GetMapping("/distribuir/{id}")
    public ModelAndView getForm(@PathVariable Long id, @RequestParam(required = false) Long colegiadoId) {

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
    public ModelAndView addDistribuicao(@PathVariable Long id,  @RequestParam Long colegiadoId, @RequestParam Long professorId, RedirectAttributes redirectAttributes ) {

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
    @GetMapping("/reunioes")
    public ModelAndView listarReunioes() {
        ModelAndView mv = new ModelAndView("reunioes/coordenadores/list");
        mv.addObject("reunioes", reuniaoService.findAll());
        return mv;
    }

    @GetMapping("/reunioes/form")
    public ModelAndView formReunioes() {
        ModelAndView mv = new ModelAndView("reunioes/coordenadores/form");
        // Adicione esta linha para inicializar o th:object="${reuniao}" do HTML
        mv.addObject("reuniao", new Reuniao());
        
        mv.addObject("colegiadosItens", colegiadoService.findAll());
        
        // Filtra processos prontos para pauta (DISPONIVEIS)
        mv.addObject("processosItens", processoService.findAll().stream()
            .filter(p -> p.getStatus() == StatusProcesso.DISPONIVEL).toList());
        
        mv.addObject("professoresItens", usuarioService.findByRole(Role.PROFESSOR));
        return mv;
    }


    @PostMapping("/reunioes/save")
    public String save(Reuniao reuniao) {
        // Atualiza status dos processos para EM_PAUTA ao agendar
        reuniao.getPauta().forEach(p -> p.setStatus(StatusProcesso.EM_PAUTA));
        reuniaoService.save(reuniao);
        return "redirect:coordenadores/reunioes";
    }

    @GetMapping("reunioes/{id}/conduzir")
    public ModelAndView conduzir(@PathVariable Long id) {
        ModelAndView mv = new ModelAndView("reunioes/coordenadores/conduzir"); 
        mv.addObject("reuniao", reuniaoService.findById(id));
        return mv;
    }

    @PostMapping("reunioes/{id}/iniciar")
    public String iniciar(@PathVariable Long id) {
        reuniaoService.iniciarSessao(id);
        return "redirect:/coordenadores/reunioes/" + id + "/conduzir";
    }

    @PostMapping("reunioes/{id}/finalizar")
    public String finalizar(@PathVariable Long id) {
        reuniaoService.finalizarSessao(id);
        return "redirect:/coordenadores/reunioes";
    }

    @GetMapping
    public ModelAndView listarProcessos( 
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
}
