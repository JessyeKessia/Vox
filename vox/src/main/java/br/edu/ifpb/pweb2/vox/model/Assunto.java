package br.edu.ifpb.pweb2.vox.model;

import java.util.HashSet;
import java.util.Set;
import java.io.Serializable;

import jakarta.persistence.*;
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

    private String nome;

    @OneToMany(mappedBy = "assunto")
     private Set<Processo> processos = new HashSet<Processo>();
}

