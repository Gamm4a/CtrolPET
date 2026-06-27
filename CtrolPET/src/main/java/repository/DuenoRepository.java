package repository;

import model.Dueno;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DuenoRepository extends MongoRepository<Dueno, ObjectId> {
}
