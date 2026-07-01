package com.example.ctrolpet.model;

import com.example.ctrolpet.model.Enum.Especialidad;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Servicios")
public class Servicio {

    @MongoId
    @Field(name = "id_servicio")
    private ObjectId idServicio;
    private String tipo;
    private String descripcion;
    private Double precio;
    private Especialidad categoria;
    private Integer duracion;
    private List<String> fotos;

}
