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
    private ObjectId id_Reserva;
    private ObjectId id_Empleado;
    private LocalDateTime fecha;
    private EstadoReserva estado;
    private Mascota mascota;
    private Servicio servicio;


}
