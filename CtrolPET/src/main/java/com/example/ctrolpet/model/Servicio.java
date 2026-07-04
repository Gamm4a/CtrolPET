package com.example.ctrolpet.model;

import com.example.ctrolpet.model.Enum.Especialidad;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Servicios")
public class Servicio {

    @MongoId
    @Field(name = "id_servicio")
    private ObjectId idServicio;

    @NotBlank(message = "El tipo o nombre del servicio es obligatorio")
    @Size(min = 3, max = 100, message = "El tipo de servicio debe tener entre 3 y 100 caracteres")
    private String tipo;

    @NotBlank(message = "La descripción del servicio es obligatoria")
    @Size(min = 10, max = 500, message = "La descripción debe tener entre 10 y 500 caracteres")
    private String descripcion;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser un número mayor a cero")
    private Double precio;

    @NotNull(message = "La categoría (especialidad) es obligatoria")
    private Especialidad categoria;

    @NotNull(message = "La duración estimada es obligatoria")
    @Min(value = 5, message = "La duración mínima del servicio debe ser de 5 minutos")
    @Max(value = 480, message = "La duración no puede exceder los 480 minutos (8 horas)")
    private Integer duracion;

}
