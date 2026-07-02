package com.example.ClienteRest.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MascotaDTO {



    private String nombre;
    private String especie;
    private String raza;

    private Instant fechaNacimiento;
    //foto
    private String fotoUrl;

    private ObjectId idHistorialClinico;

}
