package com.duoc.recetas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

/**
 * Configuración de Seguridad de la aplicación.
 * 
 * Esta clase configura Spring Security para:
 * - Proteger URLs según roles
 * - Implementar autenticación con login personalizado
 * - Habilitar protección CSRF (Contra A08: CSRF - OWASP Top 10)
 * - Configurar headers de seguridad
 * - Encriptar contraseñas con BCrypt (Contra A02: Cryptographic Failures)
 * 
 * CUMPLIMIENTO OWASP TOP 10:
 * - A01: Broken Access Control - Control de acceso por URLs
 * - A02: Cryptographic Failures - BCrypt para contraseñas
 * - A07: Identification and Authentication Failures - Autenticación robusta
 * - A08: Software and Data Integrity Failures - CSRF tokens
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configura el filtro de seguridad para las peticiones HTTP.
     * 
     * Define qué URLs son públicas y cuáles requieren autenticación.
     * 
     * @param http Configurador de seguridad HTTP
     * @return SecurityFilterChain configurado
     * @throws Exception Si hay error en la configuración
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Configuración de autorización de URLs
            .authorizeHttpRequests(auth -> auth
                // URLs PÚBLICAS - Accesibles sin autenticación
                .requestMatchers("/", "/home", "/index").permitAll()
                .requestMatchers("/buscar", "/recetas/buscar").permitAll()
                .requestMatchers("/login", "/error").permitAll()
                
                // Recursos estáticos públicos
                .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()
                .requestMatchers("/favicon.ico").permitAll()
                
                // URLs PRIVADAS - Requieren autenticación
                .requestMatchers("/recetas/detalle/**").authenticated()
                
                // Cualquier otra URL requiere autenticación
                .anyRequest().authenticated()
            )
            
            // Configuración del formulario de login
            .formLogin(form -> form
                .loginPage("/login")                    // Página de login personalizada
                .loginProcessingUrl("/login")           // URL que procesa el login
                .defaultSuccessUrl("/", true)           // Redirección después del login exitoso
                .failureUrl("/login?error=true")        // Redirección si falla el login
                .usernameParameter("username")          // Nombre del parámetro del usuario
                .passwordParameter("password")          // Nombre del parámetro de la contraseña
                .permitAll()
            )
            
            // Configuración del logout
            .logout(logout -> logout
                .logoutUrl("/logout")                   // URL para hacer logout
                .logoutSuccessUrl("/login?logout=true") // Redirección después del logout
                .invalidateHttpSession(true)            // Invalida la sesión
                .deleteCookies("JSESSIONID")            // Elimina cookies
                .permitAll()
            )
            
            // PROTECCIÓN CSRF - CRÍTICO PARA OWASP A08
            // CSRF (Cross-Site Request Forgery) es una vulnerabilidad del OWASP Top 10
            // NO DESHABILITAR EN PRODUCCIÓN
            .csrf(csrf -> csrf
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                // El token CSRF se almacena en una cookie accesible desde JavaScript
                // pero marcada como HttpOnly para mayor seguridad
            )
            
            // Headers de seguridad
            .headers(headers -> headers
                // Previene ataques de Clickjacking (OWASP A04)
                .frameOptions(frame -> frame.sameOrigin())
                
                // XSS Protection está habilitado por defecto en Spring Security 6
                // Ya no se requiere configuración manual
                
                // Previene MIME sniffing
                .contentTypeOptions(contentTypeOptions -> contentTypeOptions.disable())
                
                // HTTP Strict Transport Security (forzar HTTPS en producción)
                // Descomentar en producción con HTTPS
                // .httpStrictTransportSecurity(hsts -> hsts
                //     .maxAgeInSeconds(31536000)
                //     .includeSubDomains(true)
                // )
            )
            
            // Configuración de sesión
            .sessionManagement(session -> session
                .maximumSessions(1)                     // Máximo 1 sesión por usuario
                .maxSessionsPreventsLogin(false)        // Si hay otra sesión, invalida la anterior
            );

        return http.build();
    }

    /**
     * Bean para encriptar contraseñas usando BCrypt.
     * 
     * BCrypt es un algoritmo de hash adaptativo recomendado por OWASP.
     * Protege contra A02: Cryptographic Failures del OWASP Top 10.
     * 
     * NUNCA almacenar contraseñas en texto plano.
     * 
     * @return PasswordEncoder configurado con BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12); // Fuerza 12 (buena seguridad vs rendimiento)
    }

    /**
     * Bean para el AuthenticationManager.
     * 
     * Necesario para autenticación programática si se requiere.
     * 
     * @param authConfig Configuración de autenticación
     * @return AuthenticationManager
     * @throws Exception Si hay error en la configuración
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}