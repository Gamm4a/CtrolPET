package com.example.ClienteRest.dtos;

import com.example.ClienteRest.model.Direccion;
import com.example.ClienteRest.model.Mascota;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DuenoDTO {


    private ObjectId idDueno;
    private String nombre;


    private String apellidoPaterno;


    private String apellidoMaterno;

    private String correo;
    private String contrasenia;

    private Instant fechaNacimiento;
    private String telefono;
    private List<Mascota> mascotas;
    private Direccion direccion;

}
