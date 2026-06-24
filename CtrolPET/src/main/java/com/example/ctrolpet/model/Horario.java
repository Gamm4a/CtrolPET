package com.example.ctrolpet.model;

import com.example.ctrolpet.model.Enum.DiaSemana;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Horario {
    DiaSemana dia;
    LocalTime hora_entrada;
    LocalTime hora_salida;

}
