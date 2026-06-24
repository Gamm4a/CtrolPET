package com.example.ctrolpet.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "duenos")
public class Dueño {

    @MongoId
    ObjectId id_dueño;
    String nombre;
    String ap_materno;
    String ap_paterno;
    String correo;
    String contrasenia;
    Instant fch_nacimiento;
    String telefono;
    List<Mascota> mascotas;
    Direccion direccion;

}
