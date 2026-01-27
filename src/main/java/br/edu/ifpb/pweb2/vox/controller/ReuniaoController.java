package br.edu.ifpb.pweb2.vox.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import br.edu.ifpb.pweb2.vox.enums.Role;
import br.edu.ifpb.pweb2.vox.enums.StatusProcesso;
import br.edu.ifpb.pweb2.vox.service.ProcessoService;
import br.edu.ifpb.pweb2.vox.service.UsuarioService;
import br.edu.ifpb.pweb2.vox.entity.Reuniao;
import br.edu.ifpb.pweb2.vox.service.ReuniaoService;
import br.edu.ifpb.pweb2.vox.service.ColegiadoService;

@Controller
@RequestMapping("/reunioes")
public class ReuniaoController {

    @Autowired private ReuniaoService reuniaoService;
    @Autowired private ColegiadoService colegiadoService;
    @Autowired private ProcessoService processoService;
    @Autowired private UsuarioService usuarioService;

/*     // --- VISÃO DO COORDENADOR (Agendamento e Gestão) ---

    @GetMapping("/coordenadores/reunioes")
    public ModelAndView listarParaCoordenador() {
        // Aponta para a subpasta coordenadores dentro de reunioes
        ModelAndView mv = new ModelAndView("reunioes/coordenadores/list"); 
        mv.addObject("reunioes", reuniaoService.findAll());
        return mv;
    }

    @GetMapping("/coordenadores/reunioes/form")
    public ModelAndView form(Reuniao reuniao) {
        ModelAndView mv = new ModelAndView("reunioes/coordenadores/form");
        mv.addObject("reuniao", reuniao);
        mv.addObject("colegiadosItens", colegiadoService.findAll());
        
        // Mantém a lógica de listar apenas processos DISPONIVEIS
        mv.addObject("processosItens", processoService.findAll().stream()
            .filter(p -> p.getStatus() == StatusProcesso.DISPONIVEL).toList());
        
        mv.addObject("professoresItens", usuarioService.findByRole(Role.PROFESSOR));
        return mv;
    }

    @PostMapping("/coordenadores/reunioes/save")
    public String save(Reuniao reuniao) {
        // Atualiza status dos processos para EM_PAUTA ao agendar
        reuniao.getPauta().forEach(p -> p.setStatus(StatusProcesso.EM_PAUTA));
        reuniaoService.save(reuniao);
        return "redirect:/coordenadores/reunioes";
    }
 */
    // --- VISÃO DO PROFESSOR (Participação e Visualização) ---
/* 
    @GetMapping("/professores/reunioes")
    public ModelAndView listarParaProfessor() {
        // Aponta para a subpasta professores dentro de reunioes
        ModelAndView mv = new ModelAndView("reunioes/professores/list");
        
        // Aqui você pode adicionar lógica para filtrar apenas reuniões onde o prof logado está presente
        mv.addObject("reunioes", reuniaoService.findAll()); 
        return mv;
    }
 */
    // --- MÉTODOS DE EXECUÇÃO (CONDUÇÃO) ---

/*     @GetMapping("/reunioes/{id}/conduzir")
    public ModelAndView conduzir(@PathVariable Long id) {
        // Adicione "coordenadores/" no caminho para o Spring achar o HTML
        ModelAndView mv = new ModelAndView("reunioes/coordenadores/conduzir"); 
        mv.addObject("reuniao", reuniaoService.findById(id));
        return mv;
    }

    @PostMapping("/reunioes/{id}/iniciar")
    public String iniciar(@PathVariable Long id) {
        reuniaoService.iniciarSessao(id);
        // Redireciona para a mesma rota acima
        return "redirect:/reunioes/" + id + "/conduzir";
    }
    @PostMapping("/reunioes/{id}/finalizar")
    public String finalizar(@PathVariable Long id) {
        reuniaoService.finalizarSessao(id);
        // Após finalizar, redireciona para a listagem do coordenador
        return "redirect:/coordenadores/reunioes";
    } */
}