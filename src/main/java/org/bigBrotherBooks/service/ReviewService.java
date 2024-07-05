package org.bigBrotherBooks.service;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import org.bigBrotherBooks.dto.ReviewDTO;
import org.bigBrotherBooks.model.Book;
import org.bigBrotherBooks.model.Review;
import org.bigBrotherBooks.model.User;
import org.bigBrotherBooks.repository.ReviewRepository;

@Singleton
public class ReviewService {

    private final ReviewRepository reviewRepo;
    private final BookService bookService;
    private final UserService userService;

    @Inject
    public ReviewService(ReviewRepository reviewRepo, BookService bookService, UserService userService) {
        this.reviewRepo = reviewRepo;
        this.bookService = bookService;
        this.userService = userService;
    }

    public Review getReviewById(Review.ReviewId reviewId) {
        return reviewRepo.findById(reviewId);
    }

    public void saveReview(Review review) {
        reviewRepo.persist(review);
    }

    public ReviewDTO getReview(String userName, int bookId) {
        Review review = getReviewById(new Review.ReviewId(userName, bookId));
        if (review == null) {
            return null;
        }
        return ReviewService.mapToReviewDTO(review);
    }

    @Transactional
    public boolean addReview(String userName, int bookId, ReviewDTO reviewDTO) {
        Review review = new Review();
        mapToReview(reviewDTO, review);
        review.setTime(System.currentTimeMillis());
        User user = userService.getUserById(userName);
        Book book = bookService.getBookById(bookId);
        if (user == null || book == null) {
            return false;
        }
        review.setReviewId(new Review.ReviewId(userName, bookId));
        review.addUser(user);
        review.addBook(book);
        return true;
    }

    public boolean removeReview(String userName, int bookId) {
        Review.ReviewId reviewId = new Review.ReviewId(userName, bookId);
        Review review = getReviewById(reviewId);
        if (review == null) {
            return false;
        }
        reviewRepo.deleteById(reviewId);
        return true;
    }

    @Transactional
    public boolean updateReview(String userName, int bookId, ReviewDTO reviewDTO) {
        Review review = getReviewById(new Review.ReviewId(userName, bookId));
        if (review == null) {
            return false;
        }
        mapToReview(reviewDTO, review);
        return true;
    }

    public static ReviewDTO mapToReviewDTO(Review review) {
        return new ReviewDTO(review.getReviewId(), review.getText(), review.getRating(), review.getTime(), review.getLikes());
    }

    public static void mapToReview(ReviewDTO reviewDTO, Review review) {
        review.setRating(reviewDTO.getRating());
        review.setTime(reviewDTO.getTime());
        review.setLikes(reviewDTO.getLikes());
        review.setText(reviewDTO.getText());
//        review.setBook(reviewDTO.getBookById());
//        review.setUser(reviewDTO.getUser());
    }

}
