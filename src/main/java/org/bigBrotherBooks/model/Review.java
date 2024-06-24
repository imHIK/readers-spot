package org.bigBrotherBooks.model;

import jakarta.persistence.*;
import org.bigBrotherBooks.configModels.Star;

/**
 * Represents a review of a book by a user.
 * <p>
 * Fields
 * reviewId: int
 * book: Book -> many to one
 * user: User -> many to one
 * text: String
 * rating: Star
 * time: Long
 */

@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reviewId;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "user_name")
    private User user;

    private String text;
    private Star rating;
    private Long time; // epoch time

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

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Review{" +
                "reviewId=" + reviewId +
                ", book=" + book +
                ", user=" + user +
                ", text='" + text + '\'' +
                ", rating=" + rating +
                ", time=" + time +
                '}';
    }
}
