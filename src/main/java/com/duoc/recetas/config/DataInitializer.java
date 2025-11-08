package com.duoc.recetas.config;

import com.duoc.recetas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Inicializador de datos para asegurar que los usuarios tengan contraseñas correctas.
 * 
 * Este componente se ejecuta al iniciar la aplicación y actualiza las contraseñas
 * de los usuarios existentes con hashes BCrypt correctos.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("\n========================================");
        System.out.println("🔒 INICIALIZANDO CONTRASEÑAS DE USUARIOS");
        System.out.println("========================================\n");

        // Actualizar contraseña de admin
        actualizarContraseña("admin", "admin123");

        // Actualizar contraseñas de otros usuarios
        actualizarContraseña("usuario1", "usuario123");
        actualizarContraseña("usuario2", "usuario123");
        actualizarContraseña("chef", "usuario123");

        System.out.println("\n========================================");
        System.out.println("✅ USUARIOS LISTOS PARA USAR");
        System.out.println("========================================\n");
        System.out.println("Credenciales:");
        System.out.println("  admin / admin123");
        System.out.println("  usuario1 / usuario123");
        System.out.println("  usuario2 / usuario123");
        System.out.println("  chef / usuario123");
        System.out.println("\n========================================\n");
    }

    private void actualizarContraseña(String username, String password) {
        usuarioRepository.findByUsername(username).ifPresent(usuario -> {
            String hashedPassword = passwordEncoder.encode(password);
            usuario.setPassword(hashedPassword);
            usuarioRepository.save(usuario);
            System.out.println("✅ Usuario '" + username + "' actualizado");
        });
    }
}

