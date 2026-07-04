package com.example.ctrolpet.model;


import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Duenos")
public class Dueno {

    @MongoId
    @Field(name = "id_dueno")
    private ObjectId idDueno;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido paterno es obligatorio")
    @Size(min = 2, max = 50, message = "El apellido paterno debe tener entre 2 y 50 caracteres")
    @Field(name = "apellido_paterno")
    private String apellidoPaterno;

    @Size(max = 50, message = "El apellido materno no puede superar los 50 caracteres")
    @Field(name = "apellido_materno")
    private String apellidoMaterno;

    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "El formato del correo electrónico no es válido")
    private String correo;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String contrasenia;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser una fecha en el pasado")
    @Field(name = "fecha_nacimiento")
    private Instant fechaNacimiento;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "El teléfono debe contener entre 10 y 15 dígitos numéricos (puede incluir prefijo +)")
    private String telefono;

    @Valid // Valida cada objeto 'Mascota' dentro de la lista
    private List<Mascota> mascotas;

    @NotNull(message = "La dirección es obligatoria")
    @Valid // Entra a validar las anotaciones de la clase 'Direccion'
    private Direccion direccion;

}
