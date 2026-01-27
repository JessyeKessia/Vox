package br.edu.ifpb.pweb2.vox.entity;

import br.edu.ifpb.pweb2.vox.enums.StatusReuniao;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class Reuniao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate data;

    @Enumerated(EnumType.STRING)
    private StatusReuniao status = StatusReuniao.PROGRAMADA;

    @ManyToOne
    private Colegiado colegiado;

    @ManyToMany
    @JoinTable(name = "reuniao_processos")
    private List<Processo> pauta = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "reuniao_membros")
    private List<Usuario> membros = new ArrayList<>();

    @Lob
    private byte[] ata; 
}
