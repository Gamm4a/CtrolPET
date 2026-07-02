package com.example.ClienteRest.dtos;


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
public class DuenoDTO {

    private String nombre;

    private String apellidoPaterno;
    private String apellidoMaterno;

    private String correo;
    private String contrasenia;
    private Instant fechaNacimiento;
    private String telefono;
    private List<MascotaDTO> mascotas;
    private DireccionDTO direccion;

}
