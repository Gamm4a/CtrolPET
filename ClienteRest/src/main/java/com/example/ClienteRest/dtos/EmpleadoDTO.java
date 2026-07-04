package com.example.ClienteRest.dtos;

import com.example.ClienteRest.dtos.Enum.EspecialidadDTO;
import com.example.ClienteRest.dtos.Enum.PuestoDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmpleadoDTO {

    private String idEmpleado;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido paterno es obligatorio")
    @Size(min = 2, max = 50, message = "El apellido paterno debe tener entre 2 y 50 caracteres")
    private String apellidoPaterno;

    // Opcional para flexibilidad de segundos apellidos, pero con límite de tamaño
    @Size(max = 50, message = "El apellido materno no puede superar los 50 caracteres")
    private String apellidoMaterno;

    @NotBlank(message = "La sucursal asignada es obligatoria")
    private String sucursal; // Validado como @NotBlank porque en el DTO el JSON lo envía como un texto (String)

    @NotNull(message = "La especialidad es obligatoria")
    private EspecialidadDTO especialidad; // Se usa @NotNull para objetos o Enums DTO

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String contrasenia;

    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "El formato del correo electrónico no es válido")
    private String correo;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "El teléfono debe contener entre 10 y 15 dígitos numéricos")
    private String telefono;

    @NotNull(message = "El puesto es obligatorio")
    private PuestoDTO puesto;

    @NotNull(message = "El horario es obligatorio")
    @Valid
    private HorarioDTO horarios;

}
