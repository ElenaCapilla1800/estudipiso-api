package com.elenacapilla.estudipiso.service;

import com.elenacapilla.estudipiso.model.User;
import com.elenacapilla.estudipiso.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User register(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    // Actualizar perfil — solo nombre, teléfono y foto
    public User updateProfile(Long id, User updatedData) {
        User user = findById(id);
        user.setName(updatedData.getName());
        user.setPhone(updatedData.getPhone());
        user.setProfilePhoto(updatedData.getProfilePhoto());
        return userRepository.save(user);
    }
}