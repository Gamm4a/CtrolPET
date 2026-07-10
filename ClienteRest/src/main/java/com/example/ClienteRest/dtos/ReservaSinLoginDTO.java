
package com.example.ClienteRest.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservaSinLoginDTO {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El teléfono es obligatorio")
    private String telefono;

    @NotBlank(message = "El nombre de la mascota es obligatorio")
    private String nombreMascota;

    @NotBlank(message = "La especie de la mascota es obligatoria")
    private String especieMascota;

    @NotBlank(message = "La raza de la mascota es obligatoria")
    private String razaMascota;

    @NotBlank(message = "La sucursal es obligatoria")
    private String idSucursal;

    @NotBlank(message = "El servicio es obligatorio")
    private String idServicio;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @NotNull(message = "La hora es obligatoria")
    private LocalTime hora;
}