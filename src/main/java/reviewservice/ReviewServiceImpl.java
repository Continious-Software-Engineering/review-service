package reviewservice;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import review.ReviewServiceGrpc;
import review.ReviewServiceOuterClass.*;

import static java.lang.String.valueOf;

@GrpcService
public class ReviewServiceImpl extends ReviewServiceGrpc.ReviewServiceImplBase {

    @Override
    public void addReview(AddReviewRequest request, StreamObserver<Empty> responseObserver) {
        Empty response = Empty.newBuilder().build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getReviews(GetReviewsRequest request, StreamObserver<Review> responseObserver) {
        for (int i = 0; i < 5; i++) {
            Review response = Review.newBuilder()
                    .setReviewId(valueOf(i))
                    .setUserId("user_id")
                    .setRating(i)
                    .setDescription("description")
                    .build();
            responseObserver.onNext(response);
        }
        responseObserver.onCompleted();
    }

    @Override
    public void getReview(GetReviewRequest request, StreamObserver<Review> responseObserver) {
        Review response = Review.newBuilder()
                .setReviewId("review_id")
                .setUserId("user_id")
                .setRating(1)
                .setDescription("description")
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateReview(UpdateReviewRequest request, StreamObserver<Empty> responseObserver) {
        Empty response = Empty.newBuilder().build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
