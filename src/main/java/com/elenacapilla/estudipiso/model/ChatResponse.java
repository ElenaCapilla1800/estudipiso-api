package com.elenacapilla.estudipiso.model;

// Representa la respuesta que devolvemos a la app Android tras consultar a OpenAI
public class ChatResponse {

    // Respuesta generada por el modelo de IA
    private String reply;

    // Constructor vacío necesario para Jackson
    public ChatResponse() {}

    public ChatResponse(String reply) {
        this.reply = reply;
    }

    public String getReply() { return reply; }
    public void setReply(String reply) { this.reply = reply; }
}