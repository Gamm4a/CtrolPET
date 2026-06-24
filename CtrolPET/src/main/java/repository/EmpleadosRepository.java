package repository;

import model.Empleado;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EmpleadosRepository extends MongoRepository<Empleado, ObjectId> {
}
