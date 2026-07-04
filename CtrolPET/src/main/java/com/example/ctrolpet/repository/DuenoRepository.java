package com.example.ctrolpet.repository;

import com.example.ctrolpet.model.Dueno;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DuenoRepository extends MongoRepository<Dueno, ObjectId> {

    Optional<Dueno> findByCorreo(String correo);

}

