package reviewservice;

import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import review.ReviewServiceGrpc;
import review.ReviewServiceOuterClass.*;
import reviewservice.controller.DataController;

import java.util.UUID;

import static java.lang.String.valueOf;

@Slf4j
@GrpcService
public class ReviewServiceImpl extends ReviewServiceGrpc.ReviewServiceImplBase {
  private DataController dataController;

  @Override
  public void addReview(AddReviewRequest request, StreamObserver<Empty> responseObserver) {
    log.info("ADD REVIEW");
    if (isReviewRatingInRange(request)) {
      reviewservice.model.Review newReview = createNewReview(request);
      dataController.addReview(newReview);
    };

    Empty response = Empty.newBuilder().build();
    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  @Override
  public void getReviews(GetReviewsRequest request, StreamObserver<Review> responseObserver) {
    log.info("GET REVIEWS");
    dataController.getAllReviews(request.getProductId()).forEach(review -> {
      Review response = Review.newBuilder()
              .setReviewId(review.getReviewId())
              .setProductId(review.getProductId())
              .setRating(parseStringToInt(review.getRating()))
              .setDescription(review.getDescription())
              .build();
      responseObserver.onNext(response);
    });
    responseObserver.onCompleted();
  }

  @Override
  public void deleteReview(DeleteReviewRequest request, StreamObserver<Empty> responseObserver) {
    log.info("DELETE REVIEW");
    dataController.deleteReview(request.getReviewId());

    Empty response = Empty.newBuilder().build();
    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  private boolean isReviewRatingInRange(AddReviewRequest request) {
    if (request.getRating() > 5 || request.getRating() < 1) {
      log.error("Review Rating should be in range 1...5");
      return false;
    }
    return true;
  }

  private reviewservice.model.Review createNewReview(AddReviewRequest request) {
    reviewservice.model.Review newReview = new reviewservice.model.Review();
    newReview.setReviewId(UUID.randomUUID().toString());
    newReview.setProductId(request.getProductId());
    newReview.setRating(valueOf(request.getRating()));
    newReview.setDescription(request.getDescription());
    return newReview;
  }

  private int parseStringToInt(String string) {
    try {
      return Integer.parseInt(string);
    } catch (NumberFormatException e) {
      log.error(e.toString());
      return 0;
    }
  }

  @Autowired
  public void setDataController(DataController dataController) {
    this.dataController = dataController;
  }
}