package com.example.ClienteRest.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicamentoDTO {

    private String idMedicmento;

    @NotBlank(message = "El nombre del medicamento es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre del medicamento debe tener entre 2 y 100 caracteres")
    private String nombre;

    @NotBlank(message = "La dosis es obligatoria (ej. 1 tableta, 5ml)")
    @Size(min = 1, max = 50, message = "La dosis no puede superar los 50 caracteres")
    private String dosis;

    @NotBlank(message = "La frecuencia es obligatoria (ej. Cada 8 horas)")
    @Size(min = 2, max = 100, message = "La frecuencia debe tener entre 2 y 100 caracteres")
    private String frecuencia;

    @NotBlank(message = "La duración del tratamiento es obligatoria (ej. 7 días)")
    @Size(min = 2, max = 50, message = "La duración debe tener entre 2 y 50 caracteres")
    private String duracion;



}
