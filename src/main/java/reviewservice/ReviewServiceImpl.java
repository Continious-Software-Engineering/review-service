package reviewservice;

import io.grpc.stub.StreamObserver;
import lombok.extern.java.Log;
import net.devh.boot.grpc.server.service.GrpcService;
import review.ReviewServiceGrpc;
import review.ReviewServiceOuterClass.*;

import static java.lang.String.valueOf;

@Log
@GrpcService
public class ReviewServiceImpl extends ReviewServiceGrpc.ReviewServiceImplBase {

  @Override
  public void addReview(AddReviewRequest request, StreamObserver<Empty> responseObserver) {
      log.info("ADD REVIEW: sample log");
    Empty response = Empty.newBuilder().build();
    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  @Override
  public void getReviews(GetReviewsRequest request, StreamObserver<Review> responseObserver) {
      log.info("GET REVIEWS: sample log");
    for (int i = 0; i < 5; i++) {
      Review response = Review.newBuilder()
          .setReviewId(valueOf(i))
          .setRating(i)
          .setDescription("description")
          .build();
      responseObserver.onNext(response);
    }
    responseObserver.onCompleted();
  }

  @Override
  public void deleteReview(DeleteReviewRequest request, StreamObserver<Empty> responseObserver) {
    log.info("DELETE REVIEW: sample log");
    Empty response = Empty.newBuilder().build();
    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }
}
