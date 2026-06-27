package com.example.ctrolpet.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Medicamento {

    private ObjectId idMedicmento;
    private String nombre;
    private String dosis;
    private String frecuencia;
    private String duracion;



}
