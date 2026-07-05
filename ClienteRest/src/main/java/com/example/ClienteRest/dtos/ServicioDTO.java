package com.example.ClienteRest.dtos;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.example.ClienteRest.dtos.Enum.EspecialidadDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServicioDTO {

    private String idServicio;

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
    private EspecialidadDTO categoria;

    @NotNull(message = "La duración estimada es obligatoria")
    @Min(value = 5, message = "La duración mínima del servicio debe ser de 5 minutos")
    @Max(value = 480, message = "La duración no puede exceder los 480 minutos (8 horas)")
    private Integer duracion;

}
