package br.edu.ifpb.pweb2.vox.model;

import java.io.Serializable;

// import jakarta.persistence.*;
import lombok.*;


//@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Assunto implements Serializable {
    private static final long serialVersionUID = 1L;
    
    //@Id 
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;
}

