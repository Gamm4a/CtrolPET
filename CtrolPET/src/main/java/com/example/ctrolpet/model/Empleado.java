package com.example.ctrolpet.model;

import com.example.ctrolpet.model.Enum.Puesto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "empleados")
public class Empleado {
    @MongoId
    ObjectId id_Empleado;
    String nombre;
    String ap_paterno;
    String ap_materno;
    String correo;
    String contrasenia;
    String telefono;
    Puesto puesto;
    List<Horario> horarios;

}
