package com.elenacapilla.estudipiso.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@Service
public class ChatbotService {

    // Inyectamos la API key desde application.properties (nunca hardcodeada en el código)
    @Value("${openai.api-key}")
    private String openaiApiKey;

    // URL del endpoint de Groq (compatible con el formato de OpenAI)
    private static final String OPENAI_URL = "https://api.groq.com/openai/v1/chat/completions";

    // RestTemplate es el cliente HTTP de Spring para hacer peticiones REST
    private final RestTemplate restTemplate = new RestTemplate();

    public String chat(String userMessage) {
        try {
            // ── 1. CABECERAS ──────────────────────────────────────────────
            // OpenAI requiere autenticación con Bearer token y contenido JSON
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(openaiApiKey);

            // ── 2. MENSAJE DE SISTEMA ─────────────────────────────────────
            // Define el comportamiento del asistente — es un experto en pisos para estudiantes
            Map<String, String> systemMessage = new HashMap<>();
            systemMessage.put("role", "system");
            systemMessage.put("content",
                    "Eres un asistente virtual de EstudiPiso, una app para que estudiantes " +
                            "encuentren habitaciones cerca de universidades españolas. " +
                            "Ayuda a los usuarios con dudas sobre alquiler, contratos, precios, " +
                            "zonas universitarias y cómo usar la app. " +
                            "Responde siempre en español, de forma clara y concisa.");

            // ── 3. MENSAJE DEL USUARIO ────────────────────────────────────
            // Lo que el usuario ha escrito en el chatbot de la app
            Map<String, String> userMsg = new HashMap<>();
            userMsg.put("role", "user");
            userMsg.put("content", userMessage);

            // ── 4. CUERPO DE LA PETICIÓN ──────────────────────────────────
            // Construimos el JSON que espera la API de OpenAI
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "llama-3.3-70b-versatile"); // Modelo gratuito de Groq
            requestBody.put("messages", List.of(systemMessage, userMsg));
            requestBody.put("max_tokens", 500);             // Límite de palabras en la respuesta
            requestBody.put("temperature", 0.7);            // 0 = literal, 1 = creativo

            // ── 5. PETICIÓN HTTP ──────────────────────────────────────────
            // Enviamos el JSON a OpenAI — Spring deserializa la respuesta en un Map automáticamente
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(OPENAI_URL, entity, Map.class);

            // ── 6. EXTRAER RESPUESTA ──────────────────────────────────────
            // La respuesta de OpenAI tiene estructura: choices[0].message.content
            Map<String, Object> body = response.getBody();
            List<Map<String, Object>> choices = (List<Map<String, Object>>) body.get("choices");
            Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
            return (String) message.get("content");

        } catch (Exception e) {
            // Si falla (sin conexión, API key incorrecta, límite de créditos, etc.)
            return "Lo siento, no puedo responder en este momento. Inténtalo más tarde.";
        }
    }
}