package com.example.ctrolpet.model;

import com.example.ctrolpet.model.Enum.DiaSemana;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalTime;
import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Horario {

    @NotEmpty(message = "Debes asignar al menos un día de la semana")
    private Set<DiaSemana> dias;

    @NotNull(message = "La hora de entrada es obligatoria")
    @Field(name = "hora_entrada")
    private LocalTime horaEntrada;

    @NotNull(message = "La hora de salida es obligatoria")
    @Field(name = "hora_salida")
    private LocalTime horaSalida;

}
