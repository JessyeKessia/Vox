package br.edu.ifpb.pweb2.vox.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.edu.ifpb.pweb2.vox.entity.Processo;
import br.edu.ifpb.pweb2.vox.enums.StatusProcesso;
import br.edu.ifpb.pweb2.vox.repository.ProcessoRepository;
import org.springframework.data.domain.Sort;

@Component
<<<<<<< HEAD
public class ProcessoService implements Service<Processo, Integer> {

=======
public class ProcessoService implements Service<Processo, Long> {
    
>>>>>>> e08501738ef912fd79693544bc9c5321da2e4082
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
    public Processo save(Processo processo) {
        return processoRepository.save(processo);
    }

    @Override
    public void deleteById(Long id) {
        if (processoRepository.findById(id) != null) {
            processoRepository.deleteById(id);
        } else {
            throw new RuntimeException("Processo com ID " + id + " não encontrado.");
        }
    }
<<<<<<< HEAD

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

=======
    @Override
    public Processo findById(Long id) {
        return processoRepository.findById(id).orElse(null);
    }

    public List<Processo> findForAlunoProcessos(StatusProcesso status, String assunto, boolean ordenarPorDataCriacao) {
        if (ordenarPorDataCriacao) {
            return processoRepository.findByStatusAndAssunto(status, assunto, Sort.by("dataCriacao").descending());
        } else {
            return processoRepository.findByStatusAndAssunto(status, assunto, Sort.unsorted());
        }
    }
    public List<Processo> findForCoordenadorProcessos(StatusProcesso status, Long alunoId, Long relatorId) {
        return processoRepository.findForCoordenador(status, alunoId, relatorId);
    }
>>>>>>> e08501738ef912fd79693544bc9c5321da2e4082
}