package reviewservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import reviewservice.model.Review;


@Repository
public interface ReviewRepository extends MongoRepository<Review, String> {
}