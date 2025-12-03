package br.edu.ifpb.pweb2.vox.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;

import br.edu.ifpb.pweb2.vox.enums.StatusProcesso;
import br.edu.ifpb.pweb2.vox.enums.TipoDecisao;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Processo implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_assunto")
    private Assunto assunto;

    private String numero;

    @ManyToOne
    // não pode iniciar vazio aqui!!!!! TEM QUE TER O NOME DO AUTOR DO PROCESSO!!
    @JoinColumn(name = "aluno_interessado_id", nullable = false)
    private Aluno alunoInteressado; // aluno autor do processo
    
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataCriacao = LocalDate.now();

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataDistribuicao;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataParecer;

    // descrição simples do processo
    private String descricao;

    @Enumerated(EnumType.STRING)
    private StatusProcesso status = StatusProcesso.CRIADO;

    @Lob
    private byte[] parecer;

    @Enumerated(EnumType.STRING)
    private TipoDecisao decisaoRelator;

    @ManyToOne
    @JoinColumn(name = "professor_relator_id")
    private Professor relator; // professor designado como relator

    @PrePersist
    public void gerarNumero() {
        if (this.numero == null || this.numero.isEmpty()) {
            this.numero = UUID.randomUUID().toString().substring(0, 10).toUpperCase();
        }
    }
}

