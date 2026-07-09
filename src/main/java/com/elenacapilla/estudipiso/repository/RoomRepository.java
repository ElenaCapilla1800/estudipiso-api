package com.elenacapilla.estudipiso.repository;

import com.elenacapilla.estudipiso.model.Room;
import com.elenacapilla.estudipiso.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {

    // Habitaciones de un propietario concreto
    List<Room> findByOwner(User owner);

    // Habitaciones activas por universidad
    List<Room> findByUniversityIdAndStatus(Long universityId, Room.Status status);

    // Búsqueda con filtros combinados — precio máximo y características
    @Query("SELECT r FROM Room r WHERE r.status = 'ACTIVE' " +
            "AND (:maxPrice IS NULL OR r.price <= :maxPrice) " +
            "AND (:wifiIncluded IS NULL OR r.wifiIncluded = :wifiIncluded) " +
            "AND (:billsIncluded IS NULL OR r.billsIncluded = :billsIncluded) " +
            "AND (:petsAllowed IS NULL OR r.petsAllowed = :petsAllowed)")
    List<Room> findWithFilters(
            @Param("maxPrice") Double maxPrice,
            @Param("wifiIncluded") Boolean wifiIncluded,
            @Param("billsIncluded") Boolean billsIncluded,
            @Param("petsAllowed") Boolean petsAllowed);
}