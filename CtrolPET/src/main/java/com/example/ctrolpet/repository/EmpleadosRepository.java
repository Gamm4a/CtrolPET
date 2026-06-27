package com.example.ctrolpet.repository;

import com.example.ctrolpet.model.Empleado;
import com.example.ctrolpet.model.Enum.DiaSemana;
import com.example.ctrolpet.model.Enum.Especialidad;
import com.example.ctrolpet.model.Horario;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EmpleadosRepository extends MongoRepository<Empleado, ObjectId> {
    Optional<Empleado> findByCorreo(String correo);
    @Query("{ 'especialidad': ?0, 'horarios.dias': ?1 }")
    List<Empleado> findByEspecialidadAndDias(String especialidad, String dia);
}
