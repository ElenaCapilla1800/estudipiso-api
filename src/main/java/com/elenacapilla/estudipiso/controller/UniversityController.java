package com.elenacapilla.estudipiso.controller;

import com.elenacapilla.estudipiso.model.University;
import com.elenacapilla.estudipiso.service.UniversityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/universities")
public class UniversityController {

    // Inyectamos el servicio de universidades
    @Autowired
    private UniversityService universityService;

    // POST /api/universities
    // Añadir una universidad nueva a la base de datos
    @PostMapping
    public ResponseEntity<University> create(@Valid @RequestBody University university) {
        return ResponseEntity.ok(universityService.create(university));
    }

    // GET /api/universities
    // Listar todas las universidades
    @GetMapping
    public ResponseEntity<List<University>> findAll() {
        return ResponseEntity.ok(universityService.findAll());
    }

    // GET /api/universities/city/{city}
    // Buscar universidades por ciudad (ej: Madrid, Valencia...)
    @GetMapping("/city/{city}")
    public ResponseEntity<List<University>> findByCity(@PathVariable String city) {
        return ResponseEntity.ok(universityService.findByCity(city));
    }

    // GET /api/universities/region/{region}
    // Buscar universidades por comunidad autónoma
    @GetMapping("/region/{region}")
    public ResponseEntity<List<University>> findByRegion(@PathVariable String region) {
        return ResponseEntity.ok(universityService.findByRegion(region));
    }
}