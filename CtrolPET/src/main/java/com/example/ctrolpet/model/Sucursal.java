package com.example.ctrolpet.model;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Sucursales")
public class Sucursal {

    @MongoId
    @Field(name = "id_sucursal")
    private ObjectId idSucursal;
    private String nombre;
    private Direccion direccion;
    private String telefono;
//    private String correo;
//    private String password;
    private List<ObjectId> empleados;

}
