package com.elenacapilla.estudipiso.controller;

import com.elenacapilla.estudipiso.model.Room;
import com.elenacapilla.estudipiso.service.RoomService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    // Inyectamos el servicio que contiene la lógica de negocio
    @Autowired
    private RoomService roomService;

    // POST /api/rooms?ownerId=1
    // El propietario publica una habitación nueva
    @PostMapping
    public ResponseEntity<Room> create(@Valid @RequestBody Room room,
                                       @RequestParam Long ownerId) {
        return ResponseEntity.ok(roomService.create(ownerId, room));
    }

    // GET /api/rooms?maxPrice=500&wifiIncluded=true&billsIncluded=false&petsAllowed=false
    // Búsqueda con filtros opcionales
    @GetMapping
    public ResponseEntity<List<Room>> search(
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Boolean wifiIncluded,
            @RequestParam(required = false) Boolean billsIncluded,
            @RequestParam(required = false) Boolean petsAllowed) {
        return ResponseEntity.ok(roomService.findWithFilters(maxPrice, wifiIncluded, billsIncluded, petsAllowed));
    }

    // GET /api/rooms/{id}
    // Ver el detalle de una habitación concreta
    @GetMapping("/{id}")
    public ResponseEntity<Room> findById(@PathVariable Long id) {
        return ResponseEntity.ok(roomService.findById(id));
    }

    // GET /api/rooms/owner/{ownerId}
    // Ver todas las habitaciones de un propietario
    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<Room>> findByOwner(@PathVariable Long ownerId) {
        return ResponseEntity.ok(roomService.findByOwner(ownerId));
    }

    // PUT /api/rooms/{id}?ownerId=1
    // El propietario actualiza los datos de su habitación
    @PutMapping("/{id}")
    public ResponseEntity<Room> update(@PathVariable Long id,
                                       @RequestParam Long ownerId,
                                       @Valid @RequestBody Room room) {
        return ResponseEntity.ok(roomService.update(id, ownerId, room));
    }

    // DELETE /api/rooms/{id}?ownerId=1
    // El propietario elimina su habitación
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id,
                                       @RequestParam Long ownerId) {
        roomService.delete(id, ownerId);
        return ResponseEntity.noContent().build();
    }
}