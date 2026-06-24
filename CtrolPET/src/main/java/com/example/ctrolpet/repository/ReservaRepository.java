package com.example.ctrolpet.repository;


import com.example.ctrolpet.model.Reserva;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReservaRepository extends MongoRepository<Reserva, ObjectId> {


}
