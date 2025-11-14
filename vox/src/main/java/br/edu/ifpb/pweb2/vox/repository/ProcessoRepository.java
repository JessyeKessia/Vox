package br.edu.ifpb.pweb2.vox.repository;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;


import org.springframework.stereotype.Component;

import br.edu.ifpb.pweb2.vox.model.Processo;
import br.edu.ifpb.pweb2.vox.types.StatusProcesso;

@Component
public class ProcessoRepository {
    private Map<Integer, Processo> repositorio = new HashMap<Integer, Processo>();

    public Processo findById(Integer id) {
        return repositorio.get(id);
    }

    public Processo save(Processo processo) {
        // garante status padrão ao criar novo processo
        if (processo.getStatus() == null) {
            processo.setStatus(StatusProcesso.CRIADO);
        }
        Integer id = null;

        id = (processo.getId() == null) ? this.getMaxId() + 1 : processo.getId();
        processo.setId(id);
        repositorio.put(id, processo);
        return processo;
    }

    public List<Processo> findAll() {
        List<Processo> processos = repositorio.values().stream().collect(Collectors.toList());
        return processos;
    }
    
    public Integer getMaxId() {
        List<Processo> processos = findAll();
        if (processos == null || processos.isEmpty())
        return 1;
        Processo processoMaxId = processos.stream().max(Comparator.comparing(Processo::getId)).orElseThrow(NoSuchElementException::new);
        return processoMaxId.getId() == null ? 1 : processoMaxId.getId() + 1;
    }
    public void deleteById(Integer id) {
        repositorio.remove(id);
    }
}
