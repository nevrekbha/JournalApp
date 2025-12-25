package net.engineeringdigest.journalingApp.repository;

import net.engineeringdigest.journalingApp.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, ObjectId> {

    User findByName(String name);

    void deleteByName(String name);
}
