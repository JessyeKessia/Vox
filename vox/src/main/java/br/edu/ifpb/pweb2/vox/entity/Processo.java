package br.edu.ifpb.pweb2.vox.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.format.annotation.DateTimeFormat;

import br.edu.ifpb.pweb2.vox.types.StatusProcesso;
import br.edu.ifpb.pweb2.vox.types.TipoDecisao;


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
    
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataRecepcao = LocalDate.ofEpochDay(
        ThreadLocalRandom.current().nextLong(
                LocalDate.of(2025, 1, 1).toEpochDay(),
                LocalDate.of(2025, 12, 31).toEpochDay()
        )
    );

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataDistribuicao;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataParecer;

    private String descricao;

    @Enumerated(EnumType.STRING)
    private StatusProcesso status = StatusProcesso.CRIADO;

    @Lob
    private byte[] parecer;

    @Enumerated(EnumType.STRING)
    private TipoDecisao decisaoRelator;

    @PrePersist
    public void gerarNumero() {
        if (this.numero == null || this.numero.isEmpty()) {
            this.numero = UUID.randomUUID().toString().substring(0, 10).toUpperCase();
        }
    }
}

