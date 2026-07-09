package com.elenacapilla.estudipiso.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

// @Component: Spring lo detecta automáticamente y lo registra como bean
// OncePerRequestFilter: garantiza que este filtro se ejecuta UNA SOLA VEZ por petición HTTP
@Component
public class JwtFilter extends OncePerRequestFilter {

    // Inyectamos JwtUtil para poder validar y leer el token
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Leemos la cabecera "Authorization" de la petición HTTP
        // Ejemplo: Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String email = null;

        // Verificamos que la cabecera existe y empieza por "Bearer "
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // Extraemos solo el token (quitamos los 7 primeros caracteres: "Bearer ")
            token = authHeader.substring(7);
            try {
                // Extraemos el email del payload del token
                email = jwtUtil.extractEmail(token);
            } catch (Exception e) {
                // Si el token está mal formado o ha expirado, ignoramos sin lanzar error
            }
        }

        // Solo autenticamos si tenemos email y no hay ya una autenticación activa en el contexto
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (jwtUtil.isValid(token, email)) {
                // Creamos el objeto de autenticación con el email como principal
                // List.of() → lista vacía de roles/authorities (por ahora no los usamos)
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(email, null, List.of());

                // Añadimos detalles de la petición (IP, session...) al objeto de autenticación
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Guardamos la autenticación en el contexto de seguridad de Spring
                // A partir de aquí, Spring sabe que esta petición está autenticada
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Pasamos la petición al siguiente filtro o al controlador si ya no hay más filtros
        filterChain.doFilter(request, response);
    }
}