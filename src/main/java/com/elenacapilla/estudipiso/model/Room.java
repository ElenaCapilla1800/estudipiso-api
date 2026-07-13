package com.elenacapilla.estudipiso.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.util.List;

// Entidad JPA — Spring la mapea automáticamente a la tabla "rooms" de MySQL
// @Data de Lombok genera getters, setters, equals, hashCode y toString automáticamente
@Entity
@Table(name = "rooms")
@Data
public class Room {

    // Clave primaria autogenerada por la base de datos (AUTO_INCREMENT)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Título del anuncio — obligatorio
    @NotBlank
    private String title;

    // Descripción detallada de la habitación — obligatoria
    @NotBlank
    private String description;

    // Precio mensual en euros — obligatorio y debe ser positivo
    @NotNull
    @Positive
    private Double price;

    // Dirección completa de la habitación — obligatoria
    @NotBlank
    private String address;

    // ── CARACTERÍSTICAS BOOLEANAS ─────────────────────────────────────────
    // Cada campo representa una característica que el propietario puede marcar al publicar

    private boolean wifiIncluded;       // WiFi incluido en el precio
    private boolean billsIncluded;      // Suministros (luz, agua, gas) incluidos
    private boolean privateBathroom;    // Baño privado para el inquilino
    private boolean petsAllowed;        // Se admiten mascotas
    private boolean smokingAllowed;     // Se permite fumar
    private boolean furnished;          // Habitación amueblada

    // Número de compañeros de piso — opcional (puede ser null si vive solo)
    private Integer roommatesCount;

    // URL de vídeo opcional (YouTube, Google Drive, etc.) para mostrar la habitación
    private String videoUrl;

    // Coordenadas geográficas — opcionales, se usan cuando el propietario
    // quiere marcar la ubicación exacta en el mapa
    private Double latitude;
    private Double longitude;

    // Si true, los estudiantes ven el botón "Ver en Google Maps" en el detalle
    // El propietario lo activa al publicar la habitación
    // Usa la dirección de texto para abrir Maps — no requiere API de pago
    private boolean showOnMap = false;

    // Estado del anuncio — controla la visibilidad en el listado
    // Por defecto ACTIVE al crear una habitación nueva
    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    public enum Status {
        ACTIVE,     // Visible para los estudiantes en el listado
        INACTIVE,   // Oculto temporalmente por el propietario
        RENTED      // Ya alquilado — no aparece en búsquedas
    }

    // Relación Many-to-One con User: muchas habitaciones pertenecen a un propietario
    // owner_id es la clave foránea en la tabla rooms
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    // Relación Many-to-One con University: universidad más cercana a la habitación
    // Es opcional — no todas las habitaciones están asociadas a una universidad
    @ManyToOne
    @JoinColumn(name = "university_id")
    private University university;

    // Lista de URLs de fotos subidas a Cloudinary
    // @ElementCollection crea automáticamente una tabla separada "room_photos"
    // con las columnas room_id y photo_url — no necesita otra entidad
    @ElementCollection
    @CollectionTable(name = "room_photos", joinColumns = @JoinColumn(name = "room_id"))
    @Column(name = "photo_url")
    private List<String> photos;
}