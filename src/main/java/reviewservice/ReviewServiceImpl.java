package reviewservice;

import io.grpc.stub.StreamObserver;
import lombok.extern.java.Log;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import review.ReviewServiceGrpc;
import review.ReviewServiceOuterClass.*;

import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.String.valueOf;
import static java.util.logging.Level.INFO;
import static jdk.nashorn.internal.objects.NativeMath.log;

@Log
@GrpcService
public class ReviewServiceImpl extends ReviewServiceGrpc.ReviewServiceImplBase {

    @Override
    public void addReview(AddReviewRequest request, StreamObserver<Empty> responseObserver) {
        log(INFO, "ADD REVIEW");
        Empty response = Empty.newBuilder().build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getReviews(GetReviewsRequest request, StreamObserver<Review> responseObserver) {
        log(INFO, "GET REVIEWS");
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
        log(INFO, "GET REVIEW");
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
        log(INFO, "UPDATE REVIEW");
        Empty response = Empty.newBuilder().build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
