package com.example.ClienteRest.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import com.example.ClienteRest.dtos.Enum.EstadoReservaDTO;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservaDTO {


    private String idEmpleado;
    private LocalDateTime fecha;
    private EstadoReservaDTO estado;
    private String idSucursal;
    private String dueno;
    private String mascota;
    private ServicioDTO servicios;


}
