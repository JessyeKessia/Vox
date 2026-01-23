package br.edu.ifpb.pweb2.vox.entity;

import br.edu.ifpb.pweb2.vox.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class Usuario implements UserDetails {
    
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String matricula;

    private String nome;
  
    private String telefone;
    
    @Email(message = "Insira um Email válido")
    @Column(unique = true, nullable = false)
    private String username;

    @NotBlank(message = "Campo Obrigatório")
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> "ROLE_" + role.name());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }

    @PrePersist
    public void gerarMatricula() {
        if (this.matricula == null || this.matricula.isBlank()) {
            int ano = LocalDate.now().getYear();
            int aleatorio = ThreadLocalRandom.current().nextInt(0, 1_000_000);
            this.matricula = String.format("%d-%06d", ano, aleatorio);
        }
    }
}




