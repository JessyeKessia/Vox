package br.edu.ifpb.pweb2.vox.model;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Assunto {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    private String nome;
}

