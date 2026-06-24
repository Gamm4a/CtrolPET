package repository;

import model.Dueño;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DueñoRepository extends MongoRepository<Dueño, ObjectId> {
}
