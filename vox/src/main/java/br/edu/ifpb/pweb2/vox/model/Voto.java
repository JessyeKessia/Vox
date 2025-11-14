package br.edu.ifpb.pweb2.vox.model;

import br.edu.ifpb.pweb2.vox.types.TipoVoto;
// import jakarta.persistence.*;
import lombok.*;

// @Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Voto {
    // @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @Enumerated(EnumType.STRING)
    private TipoVoto voto;

    private boolean ausente;
}
