package com.elenacapilla.estudipiso.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Table(name = "universities")
@Data
public class University {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nombre de la universidad
    @NotBlank
    @Column(unique = true)
    private String name;

    // Ciudad donde está la universidad
    @NotBlank
    private String city;

    // Comunidad autónoma — útil para filtros más amplios
    private String region;
}