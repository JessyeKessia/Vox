package br.edu.ifpb.pweb2.vox.service;

import br.edu.ifpb.pweb2.vox.model.Aluno;
import br.edu.ifpb.pweb2.vox.repository.AlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AlunoService implements br.edu.ifpb.pweb2.vox.service.Service<Aluno, Long> {
    @Autowired
    private AlunoRepository alunoRepository;

    @Override
    public List<Aluno> findAll() {
        return alunoRepository.findAll();
    }

    @Override
    public Aluno findById(Long id) {
        return alunoRepository.findById(id).orElse(null);
    }

    @Override
    public Aluno save(Aluno aluno) {
        if (alunoRepository.existsByLogin(aluno.getLogin()) || alunoRepository.existsByMatricula(aluno.getMatricula())) {
            throw new RuntimeException("Login ou matrícula já utilizados");
        }
        return alunoRepository.save(aluno);
    }

    @Override
    public void deleteById(Long id) {
        if (alunoRepository.existsById(id)) {
            alunoRepository.deleteById(id);
        } else {
            throw new RuntimeException("Aluno com ID " + id + " não encontrado.");
        }
    }
}
