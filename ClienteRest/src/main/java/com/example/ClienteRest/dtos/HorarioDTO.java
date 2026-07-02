package com.example.ClienteRest.dtos;

import com.example.ClienteRest.dtos.Enum.DiaSemanaDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class HorarioDTO {

    private Set<DiaSemanaDTO>  dias;

    private LocalTime horaEntrada;

    private LocalTime horaSalida;

}
