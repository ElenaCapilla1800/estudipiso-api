package com.elenacapilla.estudipiso.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    // Inyectamos el bean de Cloudinary que configuramos en CloudinaryConfig
    @Autowired
    private Cloudinary cloudinary;

    // Sube una imagen a Cloudinary y devuelve la URL pública
    public String uploadImage(MultipartFile file) throws IOException {
        // upload() envía el archivo a Cloudinary
        // ObjectUtils.asMap("folder", "estudipiso") organiza las imágenes en una carpeta
        Map result = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap("folder", "estudipiso/rooms")
        );
        // "secure_url" es la URL HTTPS de la imagen subida
        return (String) result.get("secure_url");
    }

    // Sube un vídeo a Cloudinary y devuelve la URL pública
    public String uploadVideo(MultipartFile file) throws IOException {
        Map result = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap(
                        "folder", "estudipiso/videos",
                        "resource_type", "video" // importante: indicar que es vídeo
                )
        );
        return (String) result.get("secure_url");
    }

    // Elimina un archivo de Cloudinary usando su public_id
    public void delete(String publicId) throws IOException {
        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }
}