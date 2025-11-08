package com.duoc.recetas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidad que representa una receta de cocina.
 * 
 * Almacena toda la información necesaria para mostrar una receta,
 * incluyendo ingredientes, instrucciones, tiempo de preparación, etc.
 */
@Entity
@Table(name = "recetas")
@Data //Genera getter & setter
@NoArgsConstructor //Genera constructor vacío
@AllArgsConstructor //Genera constructor poblado
public class Receta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre de la receta.
     */
    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false, length = 150)
    private String nombre;

    /**
     * Tipo de cocina (ej: Italiana, Mexicana, Asiática, etc.)
     */
    @Column(length = 50)
    private String tipoCocina;

    /**
     * País de origen de la receta.
     */
    @Column(length = 50)
    private String paisOrigen;

    /**
     * Nivel de dificultad: Fácil, Intermedio, Difícil
     */
    @NotBlank(message = "La dificultad es obligatoria")
    @Column(nullable = false, length = 20)
    private String dificultad;

    /**
     * Tiempo de cocción en minutos.
     */
    @NotNull(message = "El tiempo de cocción es obligatorio")
    @Positive(message = "El tiempo debe ser positivo")
    @Column(nullable = false)
    private Integer tiempoCoccion;

    /**
     * Lista de ingredientes necesarios (texto largo).
     */
    @Column(columnDefinition = "TEXT")
    private String ingredientes;

    /**
     * Instrucciones de preparación paso a paso (texto largo).
     */
    @Column(columnDefinition = "TEXT")
    private String instrucciones;

    /**
     * URL de la fotografía de la receta.
     */
    @Column(length = 255)
    private String fotoUrl;

    /**
     * Descripción corta de la receta.
     */
    @Column(length = 500)
    private String descripcion;

    /**
     * Número de porciones que rinde la receta.
     */
    @Column
    private Integer porciones;

    /**
     * Indica si es una receta popular.
     */
    @Column(nullable = false)
    private Boolean popular = false;

    /**
     * Indica si es una receta reciente.
     */
    @Column(nullable = false)
    private Boolean reciente = false;

    /**
     * Fecha de creación de la receta.
     */
    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    /**
     * Número de visualizaciones de la receta.
     */
    @Column
    private Integer visualizaciones = 0;

    /**
     * Inicializa la fecha de creación antes de persistir.
     */
    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }
}
