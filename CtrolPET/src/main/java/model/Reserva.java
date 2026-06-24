package model;


import model.Enum.EstadoReserva;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Reserva {
    ObjectId id_Reserva;
    ObjectId id_Empleado;
    LocalDateTime fecha;
    EstadoReserva estado;
    Mascota mascota;
    Servicio servicio;


}
