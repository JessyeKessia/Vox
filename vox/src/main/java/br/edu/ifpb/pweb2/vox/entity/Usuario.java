package br.edu.ifpb.pweb2.vox.entity;

import jakarta.persistence.*;
import lombok.*;
import br.edu.ifpb.pweb2.vox.enums.Role;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    protected String nome;

    @Column(unique = true)
    protected String email;

    protected String senha;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    protected Role role;

    // verifica se o usuario tem a role especificada
    public boolean hasRole(String roleName) {
        return role.name().equalsIgnoreCase(roleName);
    }
}


