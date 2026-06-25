package com.example.ctrolpet.model;

import com.example.ctrolpet.model.Enum.DiaSemana;
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

    private Set<DiaSemana>  dias;

    @Field(name = "hora_entrada")
    private LocalTime horaEntrada;

    @Field(name = "hora_salida")
    private LocalTime horaSalida;

}
