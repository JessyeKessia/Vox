package br.edu.ifpb.pweb2.vox.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Processo {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Assunto assunto;

    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String requerimento;

    @ManyToOne
    private User aluno;

    @ManyToOne
    private User relator; // professor relator

    @Enumerated(EnumType.STRING)
    private StatusProcesso status = StatusProcesso.CRIADO;

    private LocalDateTime dataCriacao = LocalDateTime.now();
}

