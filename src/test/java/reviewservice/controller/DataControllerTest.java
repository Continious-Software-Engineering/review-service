package reviewservice.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import reviewservice.model.Review;
import reviewservice.repository.ReviewRepository;

import java.util.List;

import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class DataControllerTest {
    @InjectMocks
    private DataController underTest;
    @Mock
    private ReviewRepository reviewRepositoryMock;

    @BeforeEach
    public void setUp() {
        underTest = new DataController();
        openMocks(this);
    }

    private static Object[][] testGetAllReviewsDataProvider() {
        String productId1 = "productId1";
        String productId2 = "productId2";
        Review review1 = new Review("reviewId1", productId1, "1","descripton1");
        Review review2 = new Review("reviewId2", productId2, "2","descripton2");

        return new Object[][] {
                {of(review1, review2), productId1, review1},
                {of(review1, review2), productId2, review2},
                {of(review1), productId1, review1},
                {of(review1), productId2, null},
                {of(), productId2, null},
                {null, productId2, null},
        };
    }

    @ParameterizedTest
    @MethodSource("testGetAllReviewsDataProvider")
    public void testGetAllReviews(List<Review> reviews, String productId, Review expected) {
        when(reviewRepositoryMock.findAll()).thenReturn(reviews);

        List<Review> result = underTest.getAllReviews(productId);
        assertNotNull(result);
        if(expected == null) {
            assertTrue(result.isEmpty());
        } else {
            assertTrue(result.contains(expected));
        }
    }

    @Test
    public void testAddReview() {
        Review review = new Review("reviewId", "procudtId", "1", "descripton");
        when(reviewRepositoryMock.save(any())).thenReturn(review);
        underTest.addReview(review);
        verify(reviewRepositoryMock).save(same(review));
    }

    @Test
    public void testDeleteAddReview() {
        String reviewId = "reviewId";
        doNothing().when(reviewRepositoryMock).deleteById(any());
        underTest.deleteReview(reviewId);
        verify(reviewRepositoryMock).deleteById(same(reviewId));
    }
}