package com.elenacapilla.estudipiso.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "rooms")
@Data
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    // Precio mensual en euros
    @NotNull
    @Positive
    private Double price;

    // Dirección completa
    @NotBlank
    private String address;

    // Características de la habitación
    private boolean wifiIncluded;
    private boolean billsIncluded;      // suministros incluidos
    private boolean privateBathroom;
    private boolean petsAllowed;
    private boolean smokingAllowed;
    private boolean furnished;          // amueblada

    // Número de compañeros de piso
    private Integer roommatesCount;

    // URL del vídeo (YouTube, Google Drive, etc.)
    private String videoUrl;

    // Coordenadas geográficas para mostrar la ubicación en Google Maps
    private Double latitude;
    private Double longitude;

    // Estado del anuncio — solo se muestran los activos
    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    public enum Status {
        ACTIVE,     // visible para estudiantes
        INACTIVE,   // oculto temporalmente por el propietario
        RENTED      // ya alquilado
    }

    // Relación con el propietario — muchas habitaciones pertenecen a un propietario
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    // Relación con la universidad más cercana
    @ManyToOne
    @JoinColumn(name = "university_id")
    private University university;

    // Lista de fotos — una habitación puede tener varias fotos
    // @ElementCollection guarda la lista en una tabla separada automáticamente
    @ElementCollection
    @CollectionTable(name = "room_photos", joinColumns = @JoinColumn(name = "room_id"))
    @Column(name = "photo_url")
    private List<String> photos;
}