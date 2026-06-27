package com.example.ctrolpet.model;

import com.example.ctrolpet.model.Enum.Especialidad;
import com.example.ctrolpet.model.Enum.Puesto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Document(collection = "Empleados")
public class Empleado {

    @MongoId
    @Field(name = "id_empleado")
    private ObjectId idEmpleado;

    private String nombre;

    @Field(name = "apellido_paterno")
    private String apellidoPaterno;

    @Field(name = "apellido_materno")
    private String apellidoMaterno;

    private ObjectId sucursal;

    private Especialidad especialidad;

    private String contrasenia;

    private String correo;

    private String telefono;

    private Puesto puesto;

    private Horario horarios;

}
