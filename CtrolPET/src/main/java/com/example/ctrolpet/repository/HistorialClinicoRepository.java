package com.example.ctrolpet.repository;

import com.example.ctrolpet.model.HistorialClinico;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

interface HistorialClinicoRepository  extends MongoRepository<HistorialClinico, ObjectId> {


    
}
