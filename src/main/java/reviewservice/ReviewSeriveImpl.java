package reviewservice;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import review.ReviewServiceGrpc;
import review.ReviewServiceOuterClass.*;

@GrpcService
public class ReviewSeriveImpl extends ReviewServiceGrpc.ReviewServiceImplBase {

    @Override
    public void addReview(AddReviewRequest request, StreamObserver<Empty> responseObserver) {
        super.addReview(request, responseObserver);
    }

    @Override
    public void getReviews(GetReviewsRequest request, StreamObserver<Review> responseObserver) {
        super.getReviews(request, responseObserver);
    }

    @Override
    public void getReview(GetReviewRequest request, StreamObserver<Review> responseObserver) {
        Review response = Review.newBuilder()
                .setReviewId("123")
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateReview(UpdateReviewRequest request, StreamObserver<Empty> responseObserver) {
        super.updateReview(request, responseObserver);
    }
}
