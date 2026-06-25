package com.example.ctrolpet.repository;


import com.example.ctrolpet.model.Enum.EstadoReserva;
import com.example.ctrolpet.model.Mascota;
import com.example.ctrolpet.model.Reserva;
import com.example.ctrolpet.model.Servicio;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReservaRepository extends MongoRepository<Reserva, ObjectId> {

    Page<Reserva> findById_Empleado(ObjectId id, Pageable pageable);

    Page<Reserva> findByEstado(EstadoReserva estadoReserva, Pageable pageable);

    Page<Reserva> findByMascota(Mascota mascota, Pageable pageable);

    Page<Reserva> findByServicio(Servicio servicio, Pageable pageable);

}
