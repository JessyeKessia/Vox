package br.edu.ifpb.pweb2.vox.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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


import br.edu.ifpb.pweb2.vox.entity.Assunto;
import br.edu.ifpb.pweb2.vox.entity.Colegiado;
import br.edu.ifpb.pweb2.vox.entity.Usuario;
import br.edu.ifpb.pweb2.vox.enums.Role;
import br.edu.ifpb.pweb2.vox.service.AssuntoService;
import br.edu.ifpb.pweb2.vox.service.ColegiadoService;
import br.edu.ifpb.pweb2.vox.service.UsuarioService;
import jakarta.validation.Valid;


@Controller
@RequestMapping("/admin")
public class AdminController {
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private ColegiadoService colegiadoService;

    @Autowired
    private AssuntoService assuntoService;

    @ModelAttribute("roles")
    public Role[] listarRoles() {
        return Role.values();
    }
    
    @GetMapping("/usuarios")
    public ModelAndView listarUsuarios() {
        ModelAndView mv = new ModelAndView("admin/usuarios/list");
        mv.addObject("usuarios", usuarioService.findAll());
        return mv;
    }
    
    @GetMapping("/usuarios/form")
    public ModelAndView cadastrarUsuario() {
        ModelAndView mv = new ModelAndView("admin/usuarios/form");

        mv.addObject("usuario", new Usuario ());
        return mv;
    }
    
    @PostMapping("/usuarios/save")
    public ModelAndView salvarUsuario(@Valid @ModelAttribute("usuario") Usuario usuario,
            BindingResult result) {

        ModelAndView mv = new ModelAndView("admin/usuarios/form");

        // Erros de validação do Bean Validation
        if (result.hasErrors()) {
            return mv;
        }

        // Regra de negócio: email duplicado
        if (usuarioService.existsByEmail(usuario.getEmail())) {
            result.rejectValue(
                "email",
                "email.duplicado",
                "Já existe um usuário com esse email"
            );
            return mv;
        }

        // Salva
        usuarioService.save(usuario);
        return new ModelAndView("redirect:/admin/usuarios");
    }

    @GetMapping("/usuarios/edit/{id}")
    public ModelAndView atualizarUsuario(
            @PathVariable Long id,
            @Valid @ModelAttribute("usuario") Usuario usuario,
            BindingResult result) {

            ModelAndView mv = new ModelAndView("admin/usuarios/form");

            // Erros de validação do Bean Validation
            if (result.hasErrors()) {
                return mv;
            }

            // Regra de negócio: email duplicado
            if (usuarioService.existsByEmail(usuario.getEmail())) {
                result.rejectValue(
                    "email",
                    "email.duplicado",
                    "Já existe um usuário com esse email"
                );
                return mv;
            }

            // Salava o que foi editado
            usuarioService.edit(id, usuario);
            return new ModelAndView("redirect:/admin/usuarios");

    }

    @GetMapping("/usuarios/delete/{id}")
    public ModelAndView excluirUsuario(@PathVariable Long id) {
        usuarioService.deleteById(id);
        return new ModelAndView("redirect:/admin/usuarios");
    }

    /* =========================
       CRUD DE COLEGIADOS
       ========================= */

    @GetMapping("/colegiados")
    public ModelAndView listarColegiados() {
        ModelAndView mv = new ModelAndView("admin/colegiados/list");
        mv.addObject("colegiados", colegiadoService.findAll());
        return mv;
    }

    // discutir se somente professor pode ser parte do colegiado ou coordendor tbm
    @GetMapping("/colegiados/form")
    public ModelAndView novoColegiado() {
        ModelAndView mv = new ModelAndView("admin/colegiados/form");
        mv.addObject("colegiado", new Colegiado());

        // só professores e coordenadores podem ser membros
        mv.addObject(
            "professores",
            usuarioService.findAll().stream()
                .filter(u ->
                    u.getRole() == Role.PROFESSOR 
                )
                .toList()
        );

        return mv;
    }

    @PostMapping("/colegiados/save")
    public ModelAndView salvarColegiado(
            @Valid @ModelAttribute("colegiado") Colegiado colegiado, BindingResult result,
            @RequestParam(value = "membrosIds", required = false) List<Long> membrosIds) {

        if (result.hasErrors()) {
            ModelAndView mv = new ModelAndView("admin/colegiados/form");
            mv.addObject("colegiado", colegiado);
            mv.addObject(
                "professores",
                usuarioService.findAll().stream()
                    .filter(u ->
                        u.getRole() == Role.PROFESSOR
                    )
                    .toList()
            );
            return mv;
        }
        if (membrosIds != null) {

            Set<Usuario> membros = new HashSet<>(usuarioService.findUsuariosByIdIn(membrosIds));

            // (opcional mas recomendado) garante que só PROFESSORES entrem
            membros.removeIf(u -> u.getRole() != Role.PROFESSOR);

            colegiado.setMembros(membros);

            // Mantém a relação bidirecional
            for (Usuario usuario : membros) {
                usuario.getColegiados().add(colegiado);
            }
        }

        colegiadoService.save(colegiado);
        return new ModelAndView("redirect:/admin/colegiados");
    }

    @GetMapping("/colegiados/edit/{id}")
    public ModelAndView editarColegiado(@PathVariable Long id) {
        ModelAndView mv = new ModelAndView("admin/colegiados/form");

        Colegiado colegiado = colegiadoService.findById(id);
        mv.addObject("colegiado", colegiado);

        mv.addObject(
            "professores",
            usuarioService.findAll().stream()
                .filter(u ->
                    u.getRole() == Role.PROFESSOR 
                )
                .toList()
        );

        return mv;
    }

    @GetMapping("/colegiados/delete/{id}")
    public ModelAndView excluirColegiado(@PathVariable Long id) {
        colegiadoService.deleteById(id);
        return new ModelAndView("redirect:/admin/colegiados");
    }

     /* =========================
     CRUD DE ASSUNTOS
     ========================= */
    
    @GetMapping("/assuntos")
    public ModelAndView listarAssuntos() {
        ModelAndView mv = new ModelAndView("admin/assuntos/list");
        mv.addObject("assuntos", assuntoService.findAll());
        return mv;
    }


    @GetMapping("/assuntos/form")
    public ModelAndView novoAssunto() {
        ModelAndView mv = new ModelAndView("admin/assuntos/form");
        mv.addObject("assunto", new Assunto());
        return mv;
    }

    @PostMapping("/assuntos/save")
    public ModelAndView salvarAssunto(
        @Valid @ModelAttribute("assunto") Assunto assunto,
        BindingResult br) {
            
        if (br.hasErrors()) {
            ModelAndView mv = new ModelAndView("admin/assuntos/form");
            mv.addObject("assunto", assunto);
            return mv;
        }
        assuntoService.save(assunto);
        return new ModelAndView("redirect:/admin/assuntos");
    }

    @GetMapping("/assuntos/edit/{id}")
    public ModelAndView editarAssunto(@PathVariable Long id) {
        ModelAndView mv = new ModelAndView("admin/assuntos/form");
        mv.addObject("assunto", assuntoService.findById(id));
        return mv;
    }
    @GetMapping("/assuntos/delete/{id}")
    public ModelAndView excluirAssunto(@PathVariable Long id) {
        assuntoService.deleteById(id);
        return new ModelAndView("redirect:/admin/assuntos");
    }

}
