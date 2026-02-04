package br.edu.ifpb.pweb2.vox.entity;

import br.edu.ifpb.pweb2.vox.enums.StatusReuniao;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class Reuniao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "A data da reunião é obrigatória.")
    @Future(message = "A data da reunião deve ser no futuro.")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate data;

    @Enumerated(EnumType.STRING)
    private StatusReuniao status = StatusReuniao.PROGRAMADA;

    @ManyToOne
    private Colegiado colegiado;

    @ManyToMany
    @JoinTable(name = "reuniao_processos")
    private List<Processo> pauta = new ArrayList<>();

    @Transient
    private boolean jaVotou;

    @ManyToMany
    @JoinTable(
        name = "reuniao_membros",
        joinColumns = @JoinColumn(name = "reuniao_id"),
        inverseJoinColumns = @JoinColumn(name = "usuario_id") // CORRETO
    )
    private Set<Usuario> membrosPresentes = new HashSet<>();

    @OneToMany(mappedBy = "reuniao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Voto> votos = new ArrayList<>();

    @Lob
    private byte[] ata; 
}
