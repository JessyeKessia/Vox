package br.edu.ifpb.pweb2.vox.model;

// import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

import br.edu.ifpb.pweb2.vox.types.StatusReuniao;

// @Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Reuniao {
    // @Id 
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dataReuniao;

    // @Enumerated(EnumType.STRING)
    private StatusReuniao status;

    // @Lob
    private byte[] ata;

    // @ManyToOne
    private Colegiado colegiado;

    // @OneToMany(mappedBy = "reuniao")
    private List<Processo> processos;
}
