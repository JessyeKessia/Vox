package br.edu.ifpb.pweb2.vox.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Aluno {
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
}