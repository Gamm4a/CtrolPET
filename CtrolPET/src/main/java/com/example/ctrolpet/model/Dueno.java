package com.example.ctrolpet.model;


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
    private String nombre;

    @Field(name = "apellido_paterno")
    private String apellidoPaterno;

    @Field(name = "apellido_materno")
    private String apellidoMaterno;

    private String correo;
    private String contrasenia;
    @Field(name = "fecha_nacimiento")
    private Instant fechaNacimiento;
    private String telefono;
    private List<Mascota> mascotas;
    private Direccion direccion;

}
