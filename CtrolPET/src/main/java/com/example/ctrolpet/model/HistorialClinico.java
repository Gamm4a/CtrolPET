package com.example.ctrolpet.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "HistorialClinico")
public class HistorialClinico {

    @MongoId
    private ObjectId idHistorialClinico;
    private ObjectId idMascota;
    private Instant fecha;
    private String diagnostico;
    private String observaciones;
    private List<Medicamento> medicamentos;


}
