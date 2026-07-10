package com.elenacapilla.estudipiso.model;

// Representa el mensaje que el usuario envía al chatbot desde la app Android
public class ChatRequest {

    // Pregunta del usuario (ej: "¿Cuánto cuesta vivir cerca de la UAM?")
    private String message;

    // Constructor vacío necesario para que Jackson pueda deserializar el JSON
    public ChatRequest() {}

    public ChatRequest(String message) {
        this.message = message;
    }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}