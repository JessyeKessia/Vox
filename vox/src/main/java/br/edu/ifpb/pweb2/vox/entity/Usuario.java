package br.edu.ifpb.pweb2.vox.entity;

import jakarta.persistence.*;
import lombok.*;
import br.edu.ifpb.pweb2.vox.enums.Role;
import jakarta.validation.constraints.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @NotBlank(message = "O nome é obrigatório")
    @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
    protected String nome;

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Email inválido")
    @Column(unique = true)
    protected String email;

    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6, message = "A senha deve ter entre 6 e 30 caracteres")
    protected String senha;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    protected Role role;

    // verifica se o usuario tem a role especificada
    public boolean hasRole(String roleName) {
        return role.name().equalsIgnoreCase(roleName);
    }
}


