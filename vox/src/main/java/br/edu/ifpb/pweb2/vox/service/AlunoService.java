package br.edu.ifpb.pweb2.vox.service;

import br.edu.ifpb.pweb2.vox.entity.Aluno;
import br.edu.ifpb.pweb2.vox.repository.AlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
<<<<<<< HEAD
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
=======
import org.springframework.stereotype.Component;
>>>>>>> e08501738ef912fd79693544bc9c5321da2e4082
import java.util.List;

@Component
public class AlunoService implements Service<Aluno, Long> {
    @Autowired
    private AlunoRepository alunoRepository;

    @Override
    public List<Aluno> findAll() {
        return alunoRepository.findAll();
    }

    // NOVO MÉTODO: Paginação (sem @Override)
    public Page<Aluno> findAll(Pageable pageable) {
        return alunoRepository.findAll(pageable);
    }

    @Override
    public Aluno findById(Long id) {
        return alunoRepository.findById(id).orElse(null);
    }

    @Override
    public Aluno save(Aluno aluno) {
<<<<<<< HEAD
        if (alunoRepository.existsByLogin(aluno.getLogin())
                || alunoRepository.existsByMatricula(aluno.getMatricula())) {
            throw new RuntimeException("Login ou matrícula já utilizados");
=======
        if (aluno.getId() == null) {
            if (alunoRepository.existsByEmail(aluno.getEmail()) || alunoRepository.existsByMatricula(aluno.getMatricula()) ) {
            throw new RuntimeException("Email já utilizados");
        }
>>>>>>> e08501738ef912fd79693544bc9c5321da2e4082
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