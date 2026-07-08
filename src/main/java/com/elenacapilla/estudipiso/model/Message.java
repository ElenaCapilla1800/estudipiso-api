package com.elenacapilla.estudipiso.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
@Data
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String content;

    // Fecha y hora de envío — se asigna automáticamente al crear el mensaje
    private LocalDateTime sentAt = LocalDateTime.now();

    // Si el destinatario lo ha leído o no
    @Column(name = "is_read")
    private boolean read = false;

    // Quién envía el mensaje
    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    // Quién recibe el mensaje
    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    // Sobre qué habitación es el mensaje
    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;
}