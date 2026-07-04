package com.example.ClienteRest.dtos;

import com.example.ClienteRest.dtos.Enum.DiaSemanaDTO;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class HorarioDTO {

    @NotEmpty(message = "Debes asignar al menos un día de la semana en el horario")
    private Set<DiaSemanaDTO> dias; // Si DiaSemanaDTO es un Enum, Spring validará los valores automáticamente

    @NotNull(message = "La hora de entrada es obligatoria")
    private LocalTime horaEntrada;

    @NotNull(message = "La hora de salida es obligatoria")
    private LocalTime horaSalida;

}
