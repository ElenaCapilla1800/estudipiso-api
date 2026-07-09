package com.elenacapilla.estudipiso.repository;

import com.elenacapilla.estudipiso.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    // Mensajes recibidos por un usuario
    List<Message> findByReceiverIdOrderBySentAtDesc(Long receiverId);

    // Conversación entre dos usuarios sobre una habitación concreta
    List<Message> findBySenderIdAndReceiverIdAndRoomId(
            Long senderId, Long receiverId, Long roomId);

    // Mensajes no leídos de un usuario
    long countByReceiverIdAndReadFalse(Long receiverId);
}