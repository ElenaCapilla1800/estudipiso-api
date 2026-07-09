package com.elenacapilla.estudipiso.config;

import com.elenacapilla.estudipiso.security.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// @Configuration: indica que esta clase contiene configuración de Spring
// @EnableWebSecurity: activa el módulo de seguridad web de Spring
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Inyectamos nuestro filtro JWT para añadirlo a la cadena de filtros
    @Autowired
    private JwtFilter jwtFilter;

    // @Bean: Spring gestiona esta instancia. La usamos en UserService y AuthController
    // para encriptar y verificar contraseñas con BCrypt
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Desactivamos CSRF porque usamos JWT (tokens stateless), no cookies de sesión
                .csrf(csrf -> csrf.disable())

                // STATELESS: Spring NO crea ni usa sesiones HTTP
                // Cada petición debe incluir su propio token JWT
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Configuramos qué rutas son públicas y cuáles requieren autenticación
                .authorizeHttpRequests(auth -> auth
                        // Registro y login son públicos (no necesitan token)
                        .requestMatchers("/api/auth/register", "/api/auth/login").permitAll()
                        // Cualquier otra petición requiere estar autenticado (tener token válido)
                        .anyRequest().authenticated()
                )

                // Añadimos nuestro JwtFilter ANTES del filtro de autenticación por defecto de Spring
                // Así, cuando llegue una petición, primero procesamos el token JWT
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}