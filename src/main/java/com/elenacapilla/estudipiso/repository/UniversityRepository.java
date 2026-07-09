package com.elenacapilla.estudipiso.repository;

import com.elenacapilla.estudipiso.model.University;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UniversityRepository extends JpaRepository<University, Long> {
    List<University> findByCity(String city);
    List<University> findByRegion(String region);
}