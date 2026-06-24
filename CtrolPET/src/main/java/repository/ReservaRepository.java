package repository;


import model.Enum.EstadoReserva;
import model.Mascota;
import model.Reserva;
import model.Servicio;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ReservaRepository extends MongoRepository<Reserva, ObjectId> {

    Page<Reserva> findById_Empleado(ObjectId id, Pageable pageable);

    Page<Reserva> findByEstado(EstadoReserva estadoReserva, Pageable pageable);

    Page<Reserva> findByMascota(Mascota mascota, Pageable pageable);

    Page<Reserva> findByServicio(Servicio servicio, Pageable pageable);

}
