package com.elenacapilla.estudipiso.controller;

import com.elenacapilla.estudipiso.model.ChatRequest;
import com.elenacapilla.estudipiso.model.ChatResponse;
import com.elenacapilla.estudipiso.service.ChatbotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatbotController {

    @Autowired
    private ChatbotService chatbotService;

    // POST /api/chat — recibe la pregunta del usuario y devuelve la respuesta de la IA
    @PostMapping
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest request) {
        // Llamamos al servicio que conecta con OpenAI
        String reply = chatbotService.chat(request.getMessage());
        // Devolvemos la respuesta envuelta en ChatResponse
        return ResponseEntity.ok(new ChatResponse(reply));
    }
}