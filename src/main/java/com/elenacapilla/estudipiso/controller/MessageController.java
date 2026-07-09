package com.elenacapilla.estudipiso.controller;

import com.elenacapilla.estudipiso.model.Message;
import com.elenacapilla.estudipiso.service.MessageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    // Inyectamos el servicio de mensajes
    @Autowired
    private MessageService messageService;

    // POST /api/messages?senderId=1&receiverId=2&roomId=1
    // Body: "Hola, me interesa la habitación"  (texto plano)
    @PostMapping
    public ResponseEntity<Message> send(@RequestBody String content,
                                        @RequestParam Long senderId,
                                        @RequestParam Long receiverId,
                                        @RequestParam Long roomId) {
        return ResponseEntity.ok(messageService.send(senderId, receiverId, roomId, content));
    }

    // GET /api/messages/received/{userId}
    // Ver todos los mensajes recibidos por un usuario, ordenados del más reciente al más antiguo
    @GetMapping("/received/{userId}")
    public ResponseEntity<List<Message>> getReceived(@PathVariable Long userId) {
        return ResponseEntity.ok(messageService.getReceivedMessages(userId));
    }

    // GET /api/messages/conversation?senderId=1&receiverId=2&roomId=1
    // Ver la conversación completa entre dos usuarios sobre una habitación concreta
    @GetMapping("/conversation")
    public ResponseEntity<List<Message>> getConversation(@RequestParam Long senderId,
                                                         @RequestParam Long receiverId,
                                                         @RequestParam Long roomId) {
        return ResponseEntity.ok(messageService.getConversation(senderId, receiverId, roomId));
    }

    // GET /api/messages/unread/{userId}
    // Contar cuántos mensajes no leídos tiene un usuario (para el badge de notificaciones)
    @GetMapping("/unread/{userId}")
    public ResponseEntity<Long> countUnread(@PathVariable Long userId) {
        return ResponseEntity.ok(messageService.countUnread(userId));
    }

    // PATCH /api/messages/{id}/read
    // Marcar un mensaje como leído
    @PatchMapping("/{id}/read")
    public ResponseEntity<Message> markAsRead(@PathVariable Long id) {
        return ResponseEntity.ok(messageService.markAsRead(id));
    }
}