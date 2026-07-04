package com.example.ctrolpet.repository;

import com.example.ctrolpet.model.Servicio;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicioRepository extends MongoRepository<Servicio, ObjectId> {
}
