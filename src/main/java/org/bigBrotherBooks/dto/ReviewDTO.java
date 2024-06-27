package org.bigBrotherBooks.dto;

import org.bigBrotherBooks.configModels.Star;
import org.bigBrotherBooks.model.Review;

public class ReviewDTO {

    private Review.ReviewId reviewId;
    private String text;
    private Star rating;
    private Long time; // epoch time
    private int likes;

    public ReviewDTO(Review.ReviewId reviewId, String text, Star rating, Long time, int likes) {
        this.reviewId = reviewId;
        this.text = text;
        this.rating = rating;
        this.time = time;
        this.likes = likes;
    }

    public Review.ReviewId getReviewId() {
        return reviewId;
    }

    public void setReviewId(Review.ReviewId reviewId) {
        this.reviewId = reviewId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Star getRating() {
        return rating;
    }

    public void setRating(Star rating) {
        this.rating = rating;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    @Override
    public String toString() {
        return "ReviewDTO{" +
                "reviewId=" + reviewId +
                ", text='" + text + '\'' +
                ", rating=" + rating +
                ", time=" + time +
                ", likes=" + likes +
                '}';
    }
}
