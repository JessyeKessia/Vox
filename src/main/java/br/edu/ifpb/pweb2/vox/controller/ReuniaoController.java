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

    @GetMapping
    public ModelAndView listar() {
        ModelAndView mv = new ModelAndView("reunioes/list");
        mv.addObject("reunioes", reuniaoService.findAll());
        return mv;
    }

    @GetMapping("/form")
    public ModelAndView form(Reuniao reuniao) {
        ModelAndView mv = new ModelAndView("reunioes/form");
        mv.addObject("reuniao", reuniao);
        mv.addObject("colegiadosItens", colegiadoService.findAll());
        // Apenas processos DISPONIVEIS (com parecer do relator)
        mv.addObject("processosItens", processoService.findAll().stream()
            .filter(p -> p.getStatus() == StatusProcesso.DISPONIVEL).toList());
        mv.addObject("professoresItens", usuarioService.findByRole(Role.PROFESSOR));
        return mv;
    }

    @PostMapping("/save")
    public String save(Reuniao reuniao) {
        // Muda status dos processos selecionados para EM_PAUTA
        reuniao.getPauta().forEach(p -> {
            p.setStatus(StatusProcesso.EM_PAUTA);
        });
        reuniaoService.save(reuniao);
        return "redirect:/reunioes";
    }

    @GetMapping("/{id}/conduzir")
    public ModelAndView conduzir(@PathVariable Long id) {
        ModelAndView mv = new ModelAndView("reunioes/conduzir");
        mv.addObject("reuniao", reuniaoService.findById(id));
        return mv;
    }

    @PostMapping("/{id}/iniciar")
    public String iniciar(@PathVariable Long id) {
        reuniaoService.iniciarSessao(id);
        return "redirect:/reunioes/" + id + "/conduzir";
    }

    @PostMapping("/{id}/finalizar")
    public String finalizar(@PathVariable Long id) {
        reuniaoService.finalizarSessao(id);
        return "redirect:/reunioes";
    }
}