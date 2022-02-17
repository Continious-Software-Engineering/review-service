package reviewservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document
public class Review {
    @Id
    private String reviewId;
    @Field
    private String productId;
    @Field
    private String rating;
    @Field
    private String description;

    public Review() {}

    public Review(String reviewId, String productId, String rating, String description) {
        this.reviewId = reviewId;
        this.productId = productId;
        this.rating = rating;
        this.description = description;
    }

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return String.format("Review[reviewId='%s', productId='%s', rating='%s', description='%s']", reviewId, productId, rating, description);
    }
}
