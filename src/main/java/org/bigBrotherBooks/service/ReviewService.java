package org.bigBrotherBooks.service;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import org.bigBrotherBooks.dto.ReviewDTO;
import org.bigBrotherBooks.model.Book;
import org.bigBrotherBooks.model.Review;
import org.bigBrotherBooks.model.User;

@Singleton
public class ReviewService {

    private ReviewRepository reviewRepo;
    private BookService bookService;
    private UserService userService;

    @Inject
    public ReviewService(ReviewRepository reviewRepo, BookService bookService, UserService userService) {
        this.reviewRepo = reviewRepo;
        this.bookService = bookService;
        this.userService = userService;
    }

    @Transactional
    public Review getReviewById(Review.ReviewId reviewId) {
        return reviewRepo.findById(reviewId);
    }

    @Transactional
    public void saveReview(Review review) {
        reviewRepo.persist(review);
    }

    @Transactional
    public ReviewDTO getReview(String userName, int bookId) {
        Review review = getReviewById(new Review.ReviewId(userName, bookId));
        if (review == null) {
            return null;
        }
        return ReviewService.mapToReviewDTO(review);
    }

    @Transactional
    public boolean addReview(String userName, int bookId, Review review) {
        review.setTime(System.currentTimeMillis());
        User user = userService.getUserById(userName);
        Book book = bookService.getBook(bookId);
        if (user == null || book == null) {
            return false;
        }
        review.setReviewId(new Review.ReviewId(userName, bookId));
        review.addUser(user);
        review.addBook(book);
        return true;
    }

    @Transactional
    public boolean removeReview(String userName, int bookId) {
        Review.ReviewId reviewId = new Review.ReviewId(userName, bookId);
        Review review = getReviewById(reviewId);
        if (review == null) {
            return false;
        }
        deleteReview(reviewId);
        return true;
    }

    @Transactional
    public boolean updateReview(String userName, int bookId, Review review) {
        Review existingReview = getReviewById(new Review.ReviewId(userName, bookId));
        if (existingReview == null) {
            return false;
        }
        mapReviewDetails(review, existingReview);
        return true;
    }


    @Transactional
    public void deleteReview(Review.ReviewId reviewId) {
        reviewRepo.deleteById(reviewId);
    }

    public static ReviewDTO mapToReviewDTO(Review review) {
        return new ReviewDTO(review.getReviewId(), review.getText(), review.getRating(), review.getTime(), review.getLikes());
    }

    public static void mapReviewDetails(Review review, Review existingReview) {
        existingReview.setRating(review.getRating());
        existingReview.setTime(review.getTime());
        existingReview.setLikes(review.getLikes());
        existingReview.setText(review.getText());
//        existingReview.setBook(review.getBook());
//        existingReview.setUser(review.getUser());
    }

}
