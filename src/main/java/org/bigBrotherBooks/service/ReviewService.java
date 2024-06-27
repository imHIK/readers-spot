package org.bigBrotherBooks.service;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import org.bigBrotherBooks.model.Review;

@Singleton
public class ReviewService {

    private ReviewRepository reviewRepo;

    @Inject
    public ReviewService(ReviewRepository reviewRepo) {
        this.reviewRepo = reviewRepo;
    }

    @Transactional
    public Review getReviewById(int reviewId) {
        return reviewRepo.findById((long) reviewId);
    }

    @Transactional
    public void saveReview(Review review) {
        reviewRepo.persist(review);
    }

}
