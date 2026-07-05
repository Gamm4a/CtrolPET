package com.example.ctrolpet.repository;

import com.example.ctrolpet.model.HistorialClinico;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistorialClinicoRepository  extends MongoRepository<HistorialClinico, ObjectId> {


    
}
