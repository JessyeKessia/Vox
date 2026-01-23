package br.edu.ifpb.pweb2.vox.entity;


import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Professor extends Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean coordenador;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "professor_colegiado",
            joinColumns = @JoinColumn(name = "professor_id"),
            inverseJoinColumns = @JoinColumn(name = "colegiado_id")
    )
    private Set<Colegiado> colegiados = new HashSet<>();

    // checa se o professor é coordenador para acessar determinados lugares
    public boolean isCoordenador() {
        return coordenador;
    }

    public void setCoordenador(boolean coordenador) {
        this.coordenador = coordenador;
    }
    
}
