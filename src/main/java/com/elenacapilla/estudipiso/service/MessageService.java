package com.elenacapilla.estudipiso.service;

import com.elenacapilla.estudipiso.model.Message;
import com.elenacapilla.estudipiso.model.Room;
import com.elenacapilla.estudipiso.model.User;
import com.elenacapilla.estudipiso.repository.MessageRepository;
import com.elenacapilla.estudipiso.repository.RoomRepository;
import com.elenacapilla.estudipiso.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    // Enviar un mensaje entre dos usuarios sobre una habitación concreta
    // Busca el remitente, destinatario y habitación en la BD antes de guardar el mensaje
    public Message send(Long senderId, Long receiverId, Long roomId, String content) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Remitente no encontrado"));
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Destinatario no encontrado"));
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Habitación no encontrada"));

        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setRoom(room);
        message.setContent(content);

        return messageRepository.save(message);
    }

    // Obtener todos los mensajes recibidos por un usuario, del más reciente al más antiguo
    // @Transactional mantiene la sesión de Hibernate abierta durante la serialización JSON
    // Esto evita LazyInitializationException al acceder a colecciones lazy (ej: fotos de la habitación)
    @Transactional
    public List<Message> getReceivedMessages(Long userId) {
        return messageRepository.findByReceiverIdOrderBySentAtDesc(userId);
    }

    // Obtener la conversación completa entre dos usuarios sobre una habitación
    // Usamos findConversation (query personalizada) en lugar de findBySenderIdAndReceiverIdAndRoomId
    // para obtener mensajes en AMBAS direcciones — si no, el propietario no vería los mensajes del estudiante
    // @Transactional evita LazyInitializationException durante la serialización
    @Transactional
    public List<Message> getConversation(Long senderId, Long receiverId, Long roomId) {
        return messageRepository.findConversation(senderId, receiverId, roomId);
    }

    // Contar los mensajes no leídos de un usuario (para el badge de notificaciones)
    public long countUnread(Long userId) {
        return messageRepository.countByReceiverIdAndReadFalse(userId);
    }

    // Marcar un mensaje específico como leído
    // Se llama automáticamente desde Android cuando el usuario abre una conversación
    public Message markAsRead(Long messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Mensaje no encontrado"));
        message.setRead(true);
        return messageRepository.save(message);
    }
}