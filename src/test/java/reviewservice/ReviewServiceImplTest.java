package reviewservice;

import io.grpc.internal.testing.StreamRecorder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import review.ReviewServiceOuterClass.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.lang.String.valueOf;
import static org.junit.jupiter.api.Assertions.*;

class ReviewServiceImplTest {
    private ReviewServiceImpl underTest;

    @BeforeEach
    void setUp() {
        underTest = new ReviewServiceImpl();
    }

    @Test
    void addReview() throws Exception {
        AddReviewRequest addReviewRequest = AddReviewRequest.newBuilder()
                .setProductId("product_id")
                .setUserId("user_id")
                .setRating(1)
                .setDescription("description")
                .build();
        StreamRecorder<Empty> responseObserver = StreamRecorder.create();
        underTest.addReview(addReviewRequest, responseObserver);
        awaitResponse(responseObserver.awaitCompletion(5, TimeUnit.SECONDS));
        assertNull(responseObserver.getError());
        List<Empty> results = responseObserver.getValues();
        assertEmpty(results);
    }

    @Test
    void getReviews() throws Exception {
        GetReviewsRequest getReviewsRequest = GetReviewsRequest.newBuilder()
                .setProductId("product_id")
                .build();
        StreamRecorder<Review> responseObserver = StreamRecorder.create();
        underTest.getReviews(getReviewsRequest, responseObserver);
        awaitResponse(responseObserver.awaitCompletion(5, TimeUnit.SECONDS));
        assertNull(responseObserver.getError());
        List<Review> results = responseObserver.getValues();
        assertReviews(results);
    }

    @Test
    void getReview() throws Exception {
        GetReviewRequest getReviewRequest = GetReviewRequest.newBuilder()
                .setReviewId("review_id")
                .build();
        StreamRecorder<Review> responseObserver = StreamRecorder.create();
        underTest.getReview(getReviewRequest, responseObserver);
        awaitResponse(responseObserver.awaitCompletion(5, TimeUnit.SECONDS));
        assertNull(responseObserver.getError());
        List<Review> results = responseObserver.getValues();
        assertReview(results);
    }

    @Test
    void updateReview() throws Exception {
        UpdateReviewRequest updateReviewRequest = UpdateReviewRequest.newBuilder()
                .setReviewId("review_id")
                .setRating(1)
                .setDescription("description")
                .build();
        StreamRecorder<Empty> responseObserver = StreamRecorder.create();
        underTest.updateReview(updateReviewRequest, responseObserver);
        awaitResponse(responseObserver.awaitCompletion(5, TimeUnit.SECONDS));
        assertNull(responseObserver.getError());
        List<Empty> results = responseObserver.getValues();
        assertEmpty(results);
    }

    private void assertEmpty(List<Empty> results) {
        assertEquals(1, results.size());
        assertEquals(Empty.newBuilder().build(), results.get(0));
    }

    private void assertReviews(List<Review> results) {
        assertEquals(5, results.size());
        for (int i = 0; i < results.size(); i++) {
            Review review = Review.newBuilder()
                    .setReviewId(valueOf(i))
                    .setUserId("user_id")
                    .setRating(i)
                    .setDescription("description")
                    .build();
            assertEquals(review, results.get(i));
        }
    }

    private void assertReview(List<Review> results) {
        assertEquals(1, results.size());
        Review review = Review.newBuilder()
                .setReviewId("review_id")
                .setUserId("user_id")
                .setRating(1)
                .setDescription("description")
                .build();
        assertEquals(review, results.get(0));
    }

    private void awaitResponse(boolean responseObserver) {
        if (!responseObserver) {
            fail("The call did not terminate in time");
        }
    }
}