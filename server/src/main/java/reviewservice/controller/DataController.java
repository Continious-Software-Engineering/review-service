package reviewservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;
import reviewservice.model.Review;
import reviewservice.repository.ReviewRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@EnableJpaRepositories("reviewservice.repository")
@Component
public class DataController {
    private ReviewRepository reviewRepository;

    public List<Review> getAllReviews(String productId) {
        List<Review> reviews = new ArrayList<>();
        try {
            reviews = reviewRepository.findAll().stream()
                    .filter(review -> productId.equals(review.getProductId()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error(e.toString());
        }
        return reviews;
    }

    public void addReview(Review review) {
        try {
            reviewRepository.save(review);
        } catch (Exception e) {
            log.error(e.toString());
        }
    }

    public void deleteReview(String reviewId) {
        try {
            reviewRepository.deleteById(reviewId);
        } catch (Exception e) {
            log.error(e.toString());
        }
    }

    @Autowired
    public void setReviewRepository(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }
}
