package com.example.ctrolpet.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Mascota {

    @Field(name = "id_mascota")
    private ObjectId idMascota;

    @NotBlank(message = "El nombre de la mascota es obligatorio")
    @Size(min = 2, max = 50, message = "El nombre de la mascota debe tener entre 2 y 50 caracteres")
    private String nombre;

    @NotBlank(message = "La especie es obligatoria (ej. Perro, Gato)")
    @Size(min = 2, max = 30, message = "La especie debe tener entre 2 y 30 caracteres")
    private String especie;

    @NotBlank(message = "La raza es obligatoria (puedes usar 'Mestizo' o 'No definida')")
    @Size(min = 2, max = 50, message = "La raza debe tener entre 2 y 50 caracteres")
    private String raza;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @PastOrPresent(message = "La fecha de nacimiento no puede ser una fecha futura")
    @Field(name = "fecha_nacimiento")
    private Instant fechaNacimiento;

    @Size(max = 500, message = "La URL de la foto no puede superar los 500 caracteres")
    private String fotoUrl;

    @Field(name = "id_historial_clinico")
    private ObjectId idHistorialClinico;

}
