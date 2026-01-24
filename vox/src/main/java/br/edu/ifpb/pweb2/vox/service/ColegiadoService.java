package br.edu.ifpb.pweb2.vox.service;

import br.edu.ifpb.pweb2.vox.entity.Colegiado;
import br.edu.ifpb.pweb2.vox.entity.Professor;
import br.edu.ifpb.pweb2.vox.entity.Usuario;
import br.edu.ifpb.pweb2.vox.enums.Role;
import br.edu.ifpb.pweb2.vox.repository.ColegiadoRepository;
import br.edu.ifpb.pweb2.vox.repository.UsuarioRepository;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ColegiadoService {

    @Autowired
    private ColegiadoRepository colegiadoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService;


    public List<Colegiado> findAll() {
        return colegiadoRepository.findAll();
    }

    public Colegiado findById(Long id) {
        return colegiadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Colegiado não encontrado"));
    }

    public long countColegiados() {
        return colegiadoRepository.count();
    }

    @Transactional
    public Colegiado save(Colegiado colegiado, List<Long> membrosIds) {
        
       if (membrosIds != null && !membrosIds.isEmpty()) {
        List<Professor> membros = usuarioService.findProfessoresByIds(membrosIds);
        colegiado.setMembros(new HashSet<>(membros));

        // Mantém a relação bidirecional
        for (Professor professor : membros) {
            professor.getColegiados().add(colegiado);
        }
    }

    return colegiadoRepository.save(colegiado);
    }
    
    @Transactional
    public Colegiado update(Colegiado colegiado, List<Long> membrosIds) {
        
        Colegiado existente = colegiadoRepository.findById(colegiado.getId())
            .orElseThrow(() -> new RuntimeException("Colegiado não encontrado"));
        
        existente.setDataInicio(colegiado.getDataInicio());
        existente.setDataFim(colegiado.getDataFim());
        existente.setDescricao(colegiado.getDescricao());
        existente.setPortaria(colegiado.getPortaria());

        // Remover colegiado dos membros antigos
        for (Professor professor : existente.getMembros()) {
            professor.getColegiados().remove(existente);
        }
        existente.getMembros().clear();

        // Adicionar novos membros
        if (membrosIds != null && !membrosIds.isEmpty()) {

            List<Usuario> usuarios = usuarioRepository.findAllById(membrosIds);

            Set<Professor> novosMembros = new HashSet<>(
                usuarios.stream()
                    .filter(u ->
                        u.getRole() == Role.PROFESSOR ||
                        u.getRole() == Role.COORDENADOR
                    )
                    .filter(u -> u instanceof Professor)
                    .map(u -> (Professor) u)
                    .collect(Collectors.toSet())
            );

            existente.setMembros(novosMembros);

            for (Professor p : novosMembros) {
                p.getColegiados().add(existente);
            }

        }

        return colegiadoRepository.save(existente);
    }

    @Transactional
    public void deleteById(Long id) {
        Colegiado colegiado = findById(id);

        // Remover relacionamento com professores
        for (Professor professor : colegiado.getMembros()) {
            professor.getColegiados().remove(colegiado);
        }
        colegiadoRepository.delete(colegiado);
    }
}

