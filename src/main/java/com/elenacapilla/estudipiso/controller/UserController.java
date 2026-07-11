package com.elenacapilla.estudipiso.controller;

import com.elenacapilla.estudipiso.model.User;
import com.elenacapilla.estudipiso.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // GET /api/users/{id} — devuelve los datos del perfil
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // PUT /api/users/{id} — actualiza nombre y teléfono
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable Long id,
            @RequestBody Map<String, String> updates) {

        return userRepository.findById(id).map(user -> {
            // Solo actualizamos los campos que llegan en el body
            if (updates.containsKey("name")) user.setName(updates.get("name"));
            if (updates.containsKey("phone")) user.setPhone(updates.get("phone"));
            return ResponseEntity.ok(userRepository.save(user));
        }).orElse(ResponseEntity.notFound().build());
    }
}