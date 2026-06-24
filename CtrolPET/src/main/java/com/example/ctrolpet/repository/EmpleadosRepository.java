package com.example.ctrolpet.repository;

import com.example.ctrolpet.model.Empleado;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface EmpleadosRepository extends MongoRepository<Empleado, ObjectId> {
    Optional<Empleado> findByCorreo(String correo);
}
