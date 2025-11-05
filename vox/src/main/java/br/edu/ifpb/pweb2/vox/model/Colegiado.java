package br.edu.ifpb.pweb2.vox.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;


@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Colegiado {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    private Date dataInicio;

    @Temporal(TemporalType.DATE)
    private Date dataFim;

    private String descricao;
    private String portaria;
    private String curso;

    @ManyToMany
    @JoinTable(name = "colegiado_professor",
            joinColumns = @JoinColumn(name = "colegiado_id"),
            inverseJoinColumns = @JoinColumn(name = "professor_id"))
    private List<Professor> membros;
}