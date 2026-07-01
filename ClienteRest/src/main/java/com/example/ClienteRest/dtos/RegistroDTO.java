package com.example.ClienteRest.dtos;

import com.example.ctrolpet.model.Dueno;
import com.example.ctrolpet.model.Reserva;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistroDTO {

    private Dueno dueno;

    private Reserva reserva;

}
