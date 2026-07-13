package com.elenacapilla.estudipiso.repository;

import com.elenacapilla.estudipiso.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    // Mensajes recibidos por un usuario, del más reciente al más antiguo
    List<Message> findByReceiverIdOrderBySentAtDesc(Long receiverId);

    // Conversación en AMBAS direcciones entre dos usuarios sobre una habitación
    // La query anterior (findBySenderIdAndReceiverIdAndRoomId) solo devolvía mensajes
    // en un sentido — el propietario no veía los mensajes del estudiante
    // Con esta query JPQL obtenemos todos los mensajes entre user1 y user2 sobre roomId
    // independientemente de quién es el remitente y quién el destinatario
    @Query("SELECT m FROM Message m WHERE m.room.id = :roomId AND " +
            "((m.sender.id = :user1 AND m.receiver.id = :user2) OR " +
            "(m.sender.id = :user2 AND m.receiver.id = :user1)) " +
            "ORDER BY m.sentAt ASC")
    List<Message> findConversation(@Param("user1") Long user1,
                                   @Param("user2") Long user2,
                                   @Param("roomId") Long roomId);

    // Número de mensajes no leídos de un usuario (para el badge de notificaciones)
    long countByReceiverIdAndReadFalse(Long receiverId);
}