package com.example.ctrolpet.model;

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
    private String nombre;
    private String especie;
    private String raza;
    @Field(name = "fecha_nacimiento")
    private Instant fechaNacimiento;
    //foto
    private String foto;

    @Field(name = "id_historial_clinico")
    private ObjectId idHistorialClinico;

}
