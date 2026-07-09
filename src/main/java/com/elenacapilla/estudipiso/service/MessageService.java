package com.elenacapilla.estudipiso.service;

import com.elenacapilla.estudipiso.model.Message;
import com.elenacapilla.estudipiso.model.Room;
import com.elenacapilla.estudipiso.model.User;
import com.elenacapilla.estudipiso.repository.MessageRepository;
import com.elenacapilla.estudipiso.repository.RoomRepository;
import com.elenacapilla.estudipiso.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    // Enviar mensaje sobre una habitación
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

    // Ver mensajes recibidos por un usuario
    public List<Message> getReceivedMessages(Long userId) {
        return messageRepository.findByReceiverIdOrderBySentAtDesc(userId);
    }

    // Ver conversación entre dos usuarios sobre una habitación
    public List<Message> getConversation(Long senderId, Long receiverId, Long roomId) {
        return messageRepository.findBySenderIdAndReceiverIdAndRoomId(
                senderId, receiverId, roomId);
    }

    // Contar mensajes no leídos
    public long countUnread(Long userId) {
        return messageRepository.countByReceiverIdAndReadFalse(userId);
    }

    // Marcar mensaje como leído
    public Message markAsRead(Long messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Mensaje no encontrado"));
        message.setRead(true);
        return messageRepository.save(message);
    }
}