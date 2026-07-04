package com.example.ClienteRest.dtos;


import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import com.example.ClienteRest.dtos.Enum.EstadoReservaDTO;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservaDTO {

    private String idReserva;

    private String idEmpleado;

    @NotNull(message = "La fecha y hora de la reserva son obligatorias")
    @FutureOrPresent(message = "La fecha de la reserva no puede ser en el pasado")
    private LocalDateTime fecha; // Evita citas para fechas que ya pasaron

    @NotNull(message = "El estado de la reserva es obligatorio")
    private EstadoReservaDTO estado; // Si es un Enum, Spring valida los textos permitidos automáticamente

    @NotBlank(message = "La sucursal es obligatoria")
    private String idSucursal;

    private String dueno;

    private String mascota;

    @NotNull(message = "El servicio solicitado es obligatorio")
    @Valid
    private ServicioDTO servicios;


}
