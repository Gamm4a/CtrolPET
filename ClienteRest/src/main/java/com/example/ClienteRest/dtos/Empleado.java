package com.example.ClienteRest.dtos;

import com.example.ClienteRest.dtos.Enum.EspecialidadDTO;
import com.example.ClienteRest.dtos.Enum.PuestoDTO;

import lombok.AllArgsConstructor;
import lombok.Data;;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Empleado {

    private String nombre;

    private String apellidoPaterno;

    private String apellidoMaterno;

    private String sucursal;

    private EspecialidadDTO especialidad;

    private String contrasenia;

    private String correo;

    private String telefono;

    private PuestoDTO puesto;

    private HorarioDTO horarios;

}
