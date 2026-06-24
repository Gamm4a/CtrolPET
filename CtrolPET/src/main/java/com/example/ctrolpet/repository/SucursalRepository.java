package com.example.ctrolpet.repository;

import com.example.ctrolpet.model.Sucursal;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SucursalRepository  extends MongoRepository<Sucursal, ObjectId> {


}
