package br.edu.ifpb.pweb2.vox.model;

// import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

import br.edu.ifpb.pweb2.vox.types.StatusProcesso;
import br.edu.ifpb.pweb2.vox.types.TipoDecisao;



// @Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Processo implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Id 
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    // tem que trocar para long por causa do banco
    private Integer id;

    // @ManyToOne
    private Assunto assunto;

    private String numero;
    
    private LocalDate dataRecepcao = LocalDate.now();

    private LocalDate dataDistribuicao;

    private LocalDate dataParecer;

    private String descricao;

    private StatusProcesso status;

    // @Lob
    private byte[] parecer;

    // @Enumerated(EnumType.STRING)
    private TipoDecisao decisaoRelator;
}

