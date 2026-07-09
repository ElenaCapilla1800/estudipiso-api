package com.elenacapilla.estudipiso.service;

import com.elenacapilla.estudipiso.model.University;
import com.elenacapilla.estudipiso.repository.UniversityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UniversityService {

    // Inyectamos el repositorio para acceder a la base de datos
    @Autowired
    private UniversityRepository universityRepository;

    // Guarda una universidad nueva en la base de datos
    public University create(University university) {
        return universityRepository.save(university);
    }

    // Devuelve todas las universidades
    public List<University> findAll() {
        return universityRepository.findAll();
    }

    // Busca universidades por ciudad
    public List<University> findByCity(String city) {
        return universityRepository.findByCity(city);
    }

    // Busca universidades por comunidad autónoma
    public List<University> findByRegion(String region) {
        return universityRepository.findByRegion(region);
    }
}