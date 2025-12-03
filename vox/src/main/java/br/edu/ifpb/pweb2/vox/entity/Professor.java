package br.edu.ifpb.pweb2.vox.entity;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@PrimaryKeyJoinColumn(name = "usuario_id")
public class Professor extends Usuario {

    @NotBlank(message = "Campo obrigatório!")
    @Pattern(regexp = "^\\d{11}$", message = "O telefone deve conter apenas números (11 dígitos)")
    private String fone;

    @Column(unique = true)
    private String matricula;

    private boolean coordenador;

    // checa se o professor é coordenador para acessar determinados lugares
    public boolean isCoordenador() {
        return coordenador;
    }
    // criando a matricula do professor 
    @PrePersist
    public void gerarMatricula() {
        if (this.matricula == null || this.matricula.isBlank()) {
            int ano = LocalDate.now().getYear();
            int aleatorio = ThreadLocalRandom.current().nextInt(0, 1_000_000);
            this.matricula = String.format("%d-%06d", ano, aleatorio);
        }
    }
}
