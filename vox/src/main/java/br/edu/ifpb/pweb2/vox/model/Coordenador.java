package br.edu.ifpb.pweb2.vox.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Coordenador implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column(unique = true, nullable = false)
    private String login;

    @Column(nullable = false)
    private String senha;

    private String fone;
}
