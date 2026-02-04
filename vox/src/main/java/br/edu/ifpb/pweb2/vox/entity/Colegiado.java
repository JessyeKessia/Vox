package br.edu.ifpb.pweb2.vox.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;
// importando a anotação de validação personalizada e global
import br.edu.ifpb.pweb2.vox.validation.ColegiadoDateRange;

@ColegiadoDateRange
@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class Colegiado {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Campo obrigatório!")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    // deve ser uma data antes da data fim
    private LocalDate dataInicio;

    @NotNull(message = "Campo obrigatório!")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    // deve ser uma data depois da data de inicio
    private LocalDate dataFim;

    @NotBlank(message = "Campo obrigatório!")
    private String descricao;

    @NotBlank(message = "Campo obrigatório!")
    private String portaria;

    @NotBlank(message = "Campo obrigatório!")
    private String curso;

    @NotNull(message = "Campo obrigatório!")
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
    name = "colegiado_professor",
    joinColumns = @JoinColumn(name = "colegiado_id"),
    inverseJoinColumns = @JoinColumn(name = "professor_id"))
    private Set<Usuario> membros = new HashSet<>();
    
}