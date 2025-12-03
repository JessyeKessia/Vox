package br.edu.ifpb.pweb2.vox.service;

import java.io.ObjectInputFilter.Status;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.edu.ifpb.pweb2.vox.entity.Processo;
import br.edu.ifpb.pweb2.vox.repository.ProcessoRepository;
import br.edu.ifpb.pweb2.vox.types.StatusProcesso;

@Component
public class ProcessoService implements Service<Processo, Integer> {

    @Autowired
    ProcessoRepository processoRepository;

    @Override
    public List<Processo> findAll() {
        return processoRepository.findAll();
    }

    // NOVO MÉTODO: Paginação geral
    public Page<Processo> findAll(Pageable pageable) {
        return processoRepository.findAll(pageable);
    }

    @Override
    public Processo findById(Integer id) {
        return processoRepository.findById(id).orElse(null);
    }

    @Override
    public Processo save(Processo processo) {
        return processoRepository.save(processo);
    }

    @Override
    public void deleteById(Integer id) {
        if (processoRepository.findById(id) != null) {
            processoRepository.deleteById(id);
        } else {
            throw new RuntimeException("Processo com ID " + id + " não encontrado.");
        }
    }

    // MÉTODOS DE FILTRO: Atualizados para Page e Pageable
    public Page<Processo> findByAssunto(String assunto, Pageable pageable) {
        return processoRepository.findByAssunto(assunto, pageable);
    }

    public Page<Processo> findByStatus(StatusProcesso status, Pageable pageable) {
        return processoRepository.findByStatus(status, pageable);
    }

    // ordenar processos por data de criação
    public Page<Processo> findAllOrderedByCreationDate(Pageable pageable) {
        return processoRepository.ordProcessos(pageable);
    }

}