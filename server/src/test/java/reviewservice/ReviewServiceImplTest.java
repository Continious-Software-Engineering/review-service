package reviewservice;

import io.grpc.internal.testing.StreamRecorder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import review.ReviewServiceOuterClass.*;
import reviewservice.controller.DataController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.lang.String.valueOf;
import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class ReviewServiceImplTest {
  @InjectMocks
  private ReviewServiceImpl underTest;
  @Mock
  private DataController dataControllerMock;
  @Captor
  ArgumentCaptor<reviewservice.model.Review> reviewArgumentCaptor;

  @BeforeEach
  void setUp() {
    underTest = new ReviewServiceImpl();
    openMocks(this);
  }

  @Test
  void addReview() throws Exception {
    doNothing().when(dataControllerMock).addReview(reviewArgumentCaptor.capture());

    AddReviewRequest addReviewRequest = AddReviewRequest.newBuilder()
            .setProductId("productId")
            .setRating(1)
            .setDescription("description")
            .build();
    StreamRecorder<Empty> responseObserver = StreamRecorder.create();
    underTest.addReview(addReviewRequest, responseObserver);
    awaitResponse(responseObserver.awaitCompletion(5, TimeUnit.SECONDS));
    assertNull(responseObserver.getError());
    List<Empty> results = responseObserver.getValues();
    assertEmpty(results);

    reviewservice.model.Review value = reviewArgumentCaptor.getValue();
    assertNotNull(value.getReviewId());
    assertNotEquals("", value.getReviewId());
  }

  private static Object[][] addReviewRatingOutOfRangeDataProvider() {
    return new Object[][] {
            {0},
            {6},
    };
  }

  @ParameterizedTest
  @MethodSource("addReviewRatingOutOfRangeDataProvider")
  void addReviewRatingOutOfRange(int rating) throws Exception{
    AddReviewRequest addReviewRequest = AddReviewRequest.newBuilder()
            .setProductId("product_id")
            .setRating(rating)
            .setDescription("description")
            .build();
    StreamRecorder<Empty> responseObserver = StreamRecorder.create();
    underTest.addReview(addReviewRequest, responseObserver);
    awaitResponse(responseObserver.awaitCompletion(5, TimeUnit.SECONDS));
    assertNull(responseObserver.getError());
    List<Empty> results = responseObserver.getValues();
    assertEmpty(results);

    verifyNoInteractions(dataControllerMock);
  }

  @Test
  void getReviews() throws Exception {
    reviewservice.model.Review review = new reviewservice.model.Review("reviewId", "productId", "1", "description");
    when(dataControllerMock.getAllReviews(any())).thenReturn(of(review));

    GetReviewsRequest getReviewsRequest = GetReviewsRequest.newBuilder()
            .setProductId("productId")
            .build();
    StreamRecorder<Review> responseObserver = StreamRecorder.create();
    underTest.getReviews(getReviewsRequest, responseObserver);
    awaitResponse(responseObserver.awaitCompletion(5, TimeUnit.SECONDS));
    assertNull(responseObserver.getError());
    List<Review> results = responseObserver.getValues();

    assertNotNull(results);
    assertEquals(1, results.size());
    assertReviewValues(results.get(0), review);

    verify(dataControllerMock).getAllReviews(eq("productId"));
  }

  @Test
  void getReviewsLeer() throws Exception {
    when(dataControllerMock.getAllReviews(any())).thenReturn(new ArrayList<>());

    GetReviewsRequest getReviewsRequest = GetReviewsRequest.newBuilder()
            .setProductId("productId")
            .build();
    StreamRecorder<Review> responseObserver = StreamRecorder.create();
    underTest.getReviews(getReviewsRequest, responseObserver);
    awaitResponse(responseObserver.awaitCompletion(5, TimeUnit.SECONDS));
    assertNull(responseObserver.getError());
    List<Review> results = responseObserver.getValues();

    assertNotNull(results);
    assertEquals(0, results.size());

    verify(dataControllerMock).getAllReviews(eq("productId"));
  }

  @Test
  void getReviewsRatingIstKeineZahl() throws Exception {
    reviewservice.model.Review review = new reviewservice.model.Review("reviewId", "productId", "rating", "description");
    when(dataControllerMock.getAllReviews(any())).thenReturn(of(review));

    GetReviewsRequest getReviewsRequest = GetReviewsRequest.newBuilder()
            .setProductId("productId")
            .build();
    StreamRecorder<Review> responseObserver = StreamRecorder.create();
    underTest.getReviews(getReviewsRequest, responseObserver);
    awaitResponse(responseObserver.awaitCompletion(5, TimeUnit.SECONDS));
    assertNull(responseObserver.getError());
    List<Review> results = responseObserver.getValues();

    assertNotNull(results);
    assertEquals(1, results.size());
    assertReviewValuesRating(results.get(0), review);

    verify(dataControllerMock).getAllReviews(eq("productId"));
  }

  @Test
  void deleteReviews() throws Exception {
    doNothing().when(dataControllerMock).deleteReview(any());

    DeleteReviewRequest deleteReviewRequest = DeleteReviewRequest.newBuilder()
            .setReviewId("reviewId")
            .build();
    StreamRecorder<Empty> responseObserver = StreamRecorder.create();
    underTest.deleteReview(deleteReviewRequest, responseObserver);
    awaitResponse(responseObserver.awaitCompletion(5, TimeUnit.SECONDS));
    assertNull(responseObserver.getError());
    List<Empty> results = responseObserver.getValues();
    assertEmpty(results);

    verify(dataControllerMock).deleteReview(eq("reviewId"));
  }

  private void assertEmpty(List<Empty> results) {
    assertEquals(1, results.size());
    assertEquals(Empty.newBuilder().build(), results.get(0));
  }

  private void assertReviewValues(Review grpcReview, reviewservice.model.Review actualReview) {
    assertNotNull(grpcReview);
    assertEquals(actualReview.getReviewId(), grpcReview.getReviewId());
    assertEquals(actualReview.getProductId(), grpcReview.getProductId());
    assertEquals(actualReview.getRating(), valueOf(grpcReview.getRating()));
    assertEquals(actualReview.getDescription(), grpcReview.getDescription());
  }

  private void assertReviewValuesRating(Review grpcReview, reviewservice.model.Review actualReview) {
    assertNotNull(grpcReview);
    assertEquals(actualReview.getReviewId(), grpcReview.getReviewId());
    assertEquals(actualReview.getProductId(), grpcReview.getProductId());
    assertEquals("0", valueOf(grpcReview.getRating()));
    assertEquals(actualReview.getDescription(), grpcReview.getDescription());
  }

  private void awaitResponse(boolean responseObserver) {
    if (!responseObserver) {
      fail("The call did not terminate in time");
    }
  }
}