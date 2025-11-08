package com.duoc.recetas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * Entidad que representa un usuario del sistema.
 * 
 * Esta clase almacena la información de autenticación y autorización
 * de los usuarios que pueden acceder al sistema.
 */
@Entity
@Table(name = "usuarios")
@Data //Genera getter & setter
@NoArgsConstructor //Genera constructor vacío
@AllArgsConstructor //Genera constructor poblado
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre de usuario único para el login.
     */
    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(min = 3, max = 50, message = "El nombre de usuario debe tener entre 3 y 50 caracteres")
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    /**
     * Contraseña encriptada del usuario.
     * IMPORTANTE: Siempre debe almacenarse encriptada con BCrypt.
     */
    @NotBlank(message = "La contraseña es obligatoria")
    @Column(nullable = false)
    private String password;

    /**
     * Nombre completo del usuario (opcional).
     */
    @Column(length = 100)
    private String nombreCompleto;

    /**
     * Correo electrónico del usuario (opcional).
     */
    @Column(length = 100)
    private String email;

    /**
     * Indica si la cuenta está habilitada.
     */
    @Column(nullable = false)
    private Boolean enabled = true;

    /**
     * Roles asignados al usuario.
     * Un usuario puede tener múltiples roles.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "usuarios_roles",
        joinColumns = @JoinColumn(name = "usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "rol_id")
    )
    private Set<Rol> roles = new HashSet<>();

    /**
     * Agrega un rol al usuario.
     * 
     * @param rol Rol a agregar
     */
    public void agregarRol(Rol rol) {
        this.roles.add(rol);
    }
}
