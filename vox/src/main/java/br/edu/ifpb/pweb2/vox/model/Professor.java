package br.edu.ifpb.pweb2.vox.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
public class Professor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String fone;

    @Column(unique = true)
    private String matricula;
    
    @Column(unique = true)
    private String login;
    private String senha;
    private boolean coordenador;
}
