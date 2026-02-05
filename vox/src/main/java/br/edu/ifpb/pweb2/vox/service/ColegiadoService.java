package br.edu.ifpb.pweb2.vox.service;

import br.edu.ifpb.pweb2.vox.entity.Colegiado;
import br.edu.ifpb.pweb2.vox.entity.Processo;
import br.edu.ifpb.pweb2.vox.entity.Usuario;
import br.edu.ifpb.pweb2.vox.enums.Role;
import br.edu.ifpb.pweb2.vox.repository.ColegiadoRepository;
import br.edu.ifpb.pweb2.vox.repository.UsuarioRepository;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
    private ProcessoService processoService;


    public List<Colegiado> findAll() {
        return colegiadoRepository.findAll();
    }

    public Page<Colegiado> findAll(Pageable pageable) {
        return colegiadoRepository.findAll(pageable);
    }

    public Colegiado findById(Long id) {
        return colegiadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Colegiado não encontrado"));
    }

    public long countColegiados() {
        return colegiadoRepository.count();
    }

    @Transactional
    public Colegiado save(Colegiado colegiado) {
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

        // Remover colegiado dos membros antigos (relação bidirecional)
        for (Usuario usuario : existente.getMembros()) {
            usuario.getColegiados().remove(existente);
        }
        existente.getMembros().clear();

        // ➕ Adicionar novos membros
        if (membrosIds != null && !membrosIds.isEmpty()) {

            List<Usuario> usuarios = usuarioRepository.findAllById(membrosIds);

            Set<Usuario> novosMembros = usuarios.stream()
                .filter(u ->
                    u.getRole() == Role.PROFESSOR ||
                    u.getRole() == Role.COORDENADOR
                )
                .collect(Collectors.toSet());

            existente.setMembros(novosMembros);

            // mantém a relação bidirecional
            for (Usuario u : novosMembros) {
                u.getColegiados().add(existente);
            }
        }

        return colegiadoRepository.save(existente);
    }

    @Transactional
    public void deleteById(Long id) {
        Colegiado colegiado = findById(id);
        
        for (Usuario usuario : colegiado.getMembros()) {
            usuario.getColegiados().remove(colegiado);
        }
        colegiado.getMembros().clear(); 
        colegiadoRepository.deleteById(id);
    }

    public List<Usuario> findMembrosByColegiadoId(Long colegiadoId) {
        return colegiadoRepository.findMembrosByColegiadoId(colegiadoId);
    }

    public Set<Usuario> buscarMembrosPorProcesso(Long processoId) {

        Processo processo = processoService.findById(processoId);

        Usuario relator = processo.getRelator();
        if (relator == null) {
            throw new IllegalStateException("Processo ainda não possui relator.");
        }

        // regra do domínio: relator pertence a um colegiado e pega todos os memebros desse colegiado
        return relator.getColegiados()
            .stream()
            .findFirst()
            .orElseThrow(() -> 
                new IllegalStateException("Relator não pertence a nenhum colegiado.")
            )
            .getMembros();
    }
}


