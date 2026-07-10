package com.elenacapilla.estudipiso.controller;

import com.elenacapilla.estudipiso.model.Room;
import com.elenacapilla.estudipiso.service.CloudinaryService;
import com.elenacapilla.estudipiso.service.RoomService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    // Inyectamos el servicio de habitaciones con la lógica de negocio
    @Autowired
    private RoomService roomService;

    // Inyectamos el servicio de Cloudinary para subir fotos y vídeos
    @Autowired
    private CloudinaryService cloudinaryService;

    // POST /api/rooms?ownerId=1
    // El propietario publica una habitación nueva
    @PostMapping
    public ResponseEntity<Room> create(@Valid @RequestBody Room room,
                                       @RequestParam Long ownerId) {
        return ResponseEntity.ok(roomService.create(ownerId, room));
    }

    // GET /api/rooms?maxPrice=500&wifiIncluded=true&billsIncluded=false&petsAllowed=false
    // Búsqueda con filtros opcionales — todos los parámetros son opcionales
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
    // Ver todas las habitaciones publicadas por un propietario
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

    // POST /api/rooms/{id}/photos
    // El propietario sube una foto de su habitación
    // Recibe el archivo como multipart/form-data con el campo "file"
    @PostMapping("/{id}/photos")
    public ResponseEntity<Room> uploadPhoto(@PathVariable Long id,
                                            @RequestParam("file") MultipartFile file) {
        try {
            // Subimos la imagen a Cloudinary y obtenemos la URL pública
            String photoUrl = cloudinaryService.uploadImage(file);
            // Añadimos la URL a la lista de fotos de la habitación en la base de datos
            return ResponseEntity.ok(roomService.addPhoto(id, photoUrl));
        } catch (Exception e) {
            throw new RuntimeException("Error al subir la foto: " + e.getMessage());
        }
    }

    // POST /api/rooms/{id}/video
    // El propietario sube un vídeo de su habitación
    // Cloudinary acepta mp4, mov, avi y otros formatos de vídeo
    @PostMapping("/{id}/video")
    public ResponseEntity<Room> uploadVideo(@PathVariable Long id,
                                            @RequestParam("file") MultipartFile file) {
        try {
            // Subimos el vídeo a Cloudinary indicando resource_type=video
            String videoUrl = cloudinaryService.uploadVideo(file);
            // Recuperamos la habitación, actualizamos la URL del vídeo y guardamos
            Room room = roomService.findById(id);
            room.setVideoUrl(videoUrl);
            return ResponseEntity.ok(roomService.update(id, room.getOwner().getId(), room));
        } catch (Exception e) {
            throw new RuntimeException("Error al subir el vídeo: " + e.getMessage());
        }
    }
}