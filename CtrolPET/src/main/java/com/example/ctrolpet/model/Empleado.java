package com.example.ctrolpet.model;

import com.example.ctrolpet.model.Enum.Especialidad;
import com.example.ctrolpet.model.Enum.Puesto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Document(collection = "Empleados")
public class Empleado {

    @MongoId
    @Field(name = "id_empleado")
    private ObjectId idEmpleado;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido paterno es obligatorio")
    @Size(min = 2, max = 50, message = "El apellido de tener entre 2 y 50 caracteres")
    @Field(name = "apellido_paterno")
    private String apellidoPaterno;

    @Size(max = 50, message = "El apellido materno no puede superar los 50 caracteres")
    @Field(name = "apellido_materno")
    private String apellidoMaterno;

    @NotNull(message = "La sucursal asignada es obligatoria")
    private ObjectId sucursal;

    @NotNull(message = "La especialidad es obligatoria")
    private Especialidad especialidad;

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
    private Puesto puesto;

    @NotNull(message = "El horario es obligatorio")
    @Valid // Spring a valida sus atributos internos
    private Horario horarios;

}
