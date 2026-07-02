package com.example.ClienteRest.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicamentoDTO {

    private String nombre;
    private String dosis;
    private String frecuencia;
    private String duracion;



}
