package com.example.ClienteRest.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistorialClinicoDTO {

    private String idHistorialClinico;

    @NotBlank(message = "El ID de la mascota es obligatorio")
    private String idMascota;

    @NotNull(message = "La fecha del historial es obligatoria")
    @PastOrPresent(message = "La fecha del historial no puede ser futura")
    private Instant fecha;

    @NotBlank(message = "El diagnóstico es obligatorio")
    @Size(min = 3, max = 1000, message = "El diagnóstico debe tener entre 3 y 1000 caracteres")
    private String diagnostico;


    @Size(max = 2000, message = "Las observaciones no pueden superar los 2000 caracteres")
    private String observaciones;

    @Valid
    private List<MedicamentoDTO> medicamentos;


}
