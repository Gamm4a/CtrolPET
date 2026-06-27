package com.example.ctrolpet.model;


import com.example.ctrolpet.model.Enum.EstadoReserva;
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
    private LocalDateTime fecha;
    private EstadoReserva estado;
    @Field(name = "id_sucursal")
    private ObjectId idSucursal;
    private ObjectId dueno;
    private ObjectId mascota;
    private Servicio servicios;


}
