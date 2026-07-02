package com.example.ClienteRest.dtos;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SucursalDTO {

    private String nombre;
    private DireccionDTO direccion;
    private String telefono;
    private List<ObjectId> empleados;

}
