package org.bigBrotherBooks.dto;

import org.bigBrotherBooks.configModels.Star;

public class ReviewDTO {

    private int reviewId;
    private String text;
    private Star rating;
    private Long time; // epoch time
    private int likes;

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
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
