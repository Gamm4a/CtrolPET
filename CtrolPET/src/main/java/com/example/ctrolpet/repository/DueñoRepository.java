package com.example.ctrolpet.repository;

import com.example.ctrolpet.model.Dueño;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface DueñoRepository extends MongoRepository<Dueño, ObjectId> {
    Optional<Dueño> findByCorreo(String correo);
}
