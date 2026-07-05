package com.example.ClienteRest.dtos;
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

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SucursalDTO {

    private String idSucursal;

    @NotBlank(message = "El nombre de la sucursal es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre de la sucursal debe tener entre 3 y 100 caracteres")
    private String nombre;

    @NotNull(message = "La dirección de la sucursal es obligatoria")
    @Valid
    private DireccionDTO direccion;

    @NotBlank(message = "El teléfono de la sucursal es obligatorio")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "El teléfono debe contener entre 10 y 15 dígitos numéricos")
    private String telefono;

    private List<String> empleados;

}
