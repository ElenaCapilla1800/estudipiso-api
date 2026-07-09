package com.elenacapilla.estudipiso.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @Email
    @NotBlank
    @Column(unique = true)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank
    private String password;

    // Número de teléfono para que los estudiantes contacten con el propietario
    private String phone;

    // Foto de perfil — guardamos la URL/ruta de la imagen
    private String profilePhoto;

    // Rol del usuario: STUDENT o OWNER
    // @Enumerated indica cómo guardar el enum en MySQL
    // STRING guarda "STUDENT" o "OWNER" como texto — más legible que un número
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // Enum con los dos tipos de usuario posibles
    public enum Role {
        STUDENT,
        OWNER
    }
}