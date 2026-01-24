package br.edu.ifpb.pweb2.vox.entity;

import java.util.HashSet;
import java.util.Set;
import java.io.Serializable;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Assunto implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255)
    @NotBlank(message = "Campo obrigatório!")
    @Size(min= 50, max= 60)
    private String nome;

    @OneToMany(mappedBy = "assunto")
    private Set<Processo> processos = new HashSet<Processo>();

    public boolean isPresent() {
        return this.id != null;
    }
}

