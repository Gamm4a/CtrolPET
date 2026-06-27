package com.example.ctrolpet.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Direccion {
    private String calle;
    private String colonia;
    private String ciudad;
    private String estado;
    @Field(name = "codigo_postal")
    private String codigoPostal;
    @Field(name = "numero_casa")
    private String numeroCasa;


}
