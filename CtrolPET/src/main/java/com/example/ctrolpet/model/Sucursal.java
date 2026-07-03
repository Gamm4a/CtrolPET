package com.example.ctrolpet.model;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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

    @NotBlank(message = "El nombre de la sucursal es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre de la sucursal debe tener entre 3 y 100 caracteres")
    private String nombre;

    @NotNull(message = "La dirección de la sucursal es obligatoria")
    @Valid
    private Direccion direccion;

    @NotBlank(message = "El teléfono de la sucursal es obligatorio")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "El teléfono debe contener entre 10 y 15 dígitos numéricos")
    private String telefono;

    @Valid
    private List<ObjectId> empleados;


    //    private String correo;
//    private String password;

}
