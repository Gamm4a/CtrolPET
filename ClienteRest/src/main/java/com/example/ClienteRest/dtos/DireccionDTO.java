package com.example.ClienteRest.dtos;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DireccionDTO {
    private String calle;
    private String colonia;
    private String ciudad;
    private String estado;
    private String codigoPostal;
    private String numeroCasa;


}
