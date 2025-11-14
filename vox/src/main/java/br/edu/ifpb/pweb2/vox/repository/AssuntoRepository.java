package br.edu.ifpb.pweb2.vox.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.Comparator;
import br.edu.ifpb.pweb2.vox.model.Assunto;


import org.springframework.stereotype.Component;

@Component
public class AssuntoRepository {
    
    private Map<Integer, Assunto> repositorio = new HashMap<Integer, Assunto>();

    public Assunto findById(Integer id) {
        return repositorio.get(id);
    }
    public Assunto save(Assunto assunto) { 
        Integer id = null;

        id = (assunto.getId() == null) ? this.getMaxId() : assunto.getId();
        
        assunto.setId(id);
        
        repositorio.put(id, assunto);

        return assunto;
    }
    public List<Assunto> findAll() {
        List<Assunto> assuntos = repositorio.values().stream().collect(Collectors.toList());
        return assuntos;
    }
    public Integer getMaxId() {
        List<Assunto> assuntos = findAll();
        if (assuntos == null || assuntos.isEmpty())
        return 1;
        Assunto assuntoMaxId = assuntos.stream().max(Comparator.comparing(Assunto::getId)).orElseThrow(NoSuchElementException::new);
        return assuntoMaxId.getId() == null ? 1 : assuntoMaxId.getId() + 1;
    }

    public void deleteById(Integer id) {
        repositorio.remove(id);
    }
    
}
