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

import br.edu.ifpb.pweb2.vox.entity.Aluno;
import br.edu.ifpb.pweb2.vox.entity.Processo;
import br.edu.ifpb.pweb2.vox.entity.Professor;
import br.edu.ifpb.pweb2.vox.service.AlunoService;
import br.edu.ifpb.pweb2.vox.service.ProcessoService;
import br.edu.ifpb.pweb2.vox.service.ProfessorService;
import jakarta.servlet.http.HttpSession;
import br.edu.ifpb.pweb2.vox.enums.Role;
import br.edu.ifpb.pweb2.vox.types.StatusProcesso; // Corrigido para StatusProcesso (types)

@Controller
@RequestMapping("/coordenadores/processos")
public class CoordenadorProcessoController {

  @Autowired
  private ProcessoService processoService;

  @Autowired
  private AlunoService alunoService;

  @Autowired
  private ProfessorService professorService;

  // manda a lista de professores que não são coordenadores para o modelo
  @ModelAttribute("professoresItens")
  public List<Professor> getProfessores() {
    return professorService.findAll(); 
  }

  // manda a lista de alunos para o modelo
  @ModelAttribute("alunosItens")
  public List<Aluno> getAlunos() {
    return alunoService.findAll();
  }

  // Atribuindo a lista de status ao modelo
  @ModelAttribute("statusItens")
  public StatusProcesso[] getStatusList() {
    return StatusProcesso.values();
  }

  // pega o formulário de distribuição de processo
  @GetMapping("/distribuir/{idProcesso}")
  public ModelAndView getForm(@PathVariable Integer idProcesso) {
    ModelAndView modelAndView = new ModelAndView("coordenadores/processos/form");
    modelAndView.addObject("processo", processoService.findById(idProcesso));
    return modelAndView;
  }

  @PostMapping("/distribuir/{idProcesso}")
  public ModelAndView addDistribuicao(@PathVariable Integer idProcesso, @RequestParam Long professorId,
      RedirectAttributes redirectAttributes) {

    // acha o processo pelo id
    Processo processo = processoService.findById(idProcesso);
    // seta o relator do processo
    processo.setRelator(professorService.findById(professorId));
    // seta a data de distribuição como a data atual
    processo.setDataDistribuicao(LocalDate.now());
    // seta o status do processo como DISTRIBUIDO
    processo.setStatus(StatusProcesso.DISTRIBUIDO);
    processoService.save(processo); 

    redirectAttributes.addFlashAttribute("mensagemSucesso", "Processo distribuído com sucesso!"); 
    ModelAndView modelAndView = new ModelAndView();
    modelAndView.setViewName("redirect:/coordenadores/processos");
    return modelAndView;
  }

  @GetMapping
  public ModelAndView list(
      HttpSession session,
      @RequestParam(required = false) StatusProcesso status,
      @RequestParam(required = false) Long alunoId,
      @RequestParam(required = false) Long professorId) {

    ModelAndView modelAndView = new ModelAndView("coordenadores/processos/list");
    // mantém o que o usuário escolheu nos selects
    modelAndView.addObject("statusSelecionado", status != null ? status.name() : null);
    modelAndView.addObject("alunoSelecionado", alunoId);
    modelAndView.addObject("professorSelecionado", professorId);

    // busca usando service (lógica de filtragem fica lá)
    List<Processo> processos = processoService.findForCoordenadorProcessos(status, alunoId, professorId);

    modelAndView.addObject("processos", processos);

    return modelAndView;
  }
}
