package com.elenacapilla.estudipiso.service;

import com.elenacapilla.estudipiso.model.Room;
import com.elenacapilla.estudipiso.model.User;
import com.elenacapilla.estudipiso.repository.RoomRepository;
import com.elenacapilla.estudipiso.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    // Crear habitación — solo propietarios pueden hacerlo
    public Room create(Long ownerId, Room room) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Verificamos que el usuario es propietario
        if (owner.getRole() != User.Role.OWNER) {
            throw new RuntimeException("Solo los propietarios pueden publicar habitaciones");
        }

        room.setOwner(owner);
        return roomRepository.save(room);
    }

    // Listar todas las habitaciones activas con filtros opcionales
    public List<Room> findWithFilters(Double maxPrice, Boolean wifiIncluded,
                                      Boolean billsIncluded, Boolean petsAllowed) {
        return roomRepository.findWithFilters(maxPrice, wifiIncluded, billsIncluded, petsAllowed);
    }

    // Ver detalle de una habitación
    public Room findById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Habitación no encontrada"));
    }

    // Listar habitaciones de un propietario
    public List<Room> findByOwner(Long ownerId) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return roomRepository.findByOwner(owner);
    }

    // Actualizar habitación — solo el propietario puede editar la suya
    public Room update(Long roomId, Long ownerId, Room updatedData) {
        Room room = findById(roomId);

        if (!room.getOwner().getId().equals(ownerId)) {
            throw new RuntimeException("No tienes permiso para editar esta habitación");
        }

        room.setTitle(updatedData.getTitle());
        room.setDescription(updatedData.getDescription());
        room.setPrice(updatedData.getPrice());
        room.setAddress(updatedData.getAddress());
        room.setWifiIncluded(updatedData.isWifiIncluded());
        room.setBillsIncluded(updatedData.isBillsIncluded());
        room.setPrivateBathroom(updatedData.isPrivateBathroom());
        room.setPetsAllowed(updatedData.isPetsAllowed());
        room.setSmokingAllowed(updatedData.isSmokingAllowed());
        room.setFurnished(updatedData.isFurnished());
        room.setRoommatesCount(updatedData.getRoommatesCount());
        room.setVideoUrl(updatedData.getVideoUrl());
        room.setStatus(updatedData.getStatus());
        room.setLatitude(updatedData.getLatitude());   // Coordenada para Google Maps
        room.setLongitude(updatedData.getLongitude()); // Coordenada para Google Maps

        return roomRepository.save(room);
    }

    // Eliminar habitación — solo el propietario puede eliminar la suya
    public void delete(Long roomId, Long ownerId) {
        Room room = findById(roomId);

        if (!room.getOwner().getId().equals(ownerId)) {
            throw new RuntimeException("No tienes permiso para eliminar esta habitación");
        }

        roomRepository.delete(room);
    }

    // Añadir foto a una habitación
    public Room addPhoto(Long roomId, String photoUrl) {
        Room room = findById(roomId);
        room.getPhotos().add(photoUrl);
        return roomRepository.save(room);
    }
}