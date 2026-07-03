package com.example.ctrolpet.model;


import com.example.ctrolpet.model.Enum.EstadoReserva;
import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Reservas")
public class Reserva {

    @MongoId
    @Field(name = "id_reserva")
    private ObjectId idReserva;

    @Field(name = "id_empleado")
    private ObjectId idEmpleado;

    @NotNull(message = "La fecha y hora de la reserva son obligatorias")
    @FutureOrPresent(message = "La fecha de la reserva no puede ser en el pasado")
    private LocalDateTime fecha;

    @NotNull(message = "El estado de la reserva es obligatorio")
    private EstadoReserva estado;

    @NotNull(message = "La sucursal es obligatoria")
    @Field(name = "id_sucursal")
    private ObjectId idSucursal;

    @NotNull(message = "El dueño de la mascota es obligatorio")
    private ObjectId dueno;

    @NotNull(message = "La mascota es obligatoria")
    private ObjectId mascota;

    @NotNull(message = "El servicio solicitado es obligatorio")
    @Valid
    private Servicio servicios;


}
