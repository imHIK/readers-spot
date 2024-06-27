package org.bigBrotherBooks.model;

import jakarta.persistence.*;
import org.bigBrotherBooks.configModels.Star;

import java.io.Serializable;
import java.util.Objects;

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

    @EmbeddedId
    private ReviewId reviewId;

    @ManyToOne
    @MapsId("bookId")
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @MapsId("userName")
    @JoinColumn(name = "user_name")
    private User user;

    @Column
    private String text;

    @Column
    private Star rating;

    @Column
    private Long time; // epoch time

    @Column
    private int likes;

    public ReviewId getReviewId() {
        return reviewId;
    }

    public void setReviewId(ReviewId reviewId) {
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

    public void addBook(Book book) {
        this.book = book;
        book.getReviews().add(this);
    }

    public void removeBook() {
        book.getReviews().remove(this);
        this.book = null;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void addUser(User user) {
        this.user = user;
        user.getReviews().add(this);
    }

    public void removeUser() {
        user.getReviews().remove(this);
        this.user = null;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    @Embeddable
    public static class ReviewId implements Serializable {

        private String userName;
        private int bookId;

        public ReviewId() {
        }

        public ReviewId(String userName, int bookId) {
            this.userName = userName;
            this.bookId = bookId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public int getBookId() {
            return bookId;
        }

        public void setBookId(int bookId) {
            this.bookId = bookId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ReviewId reviewId)) return false;
            return getBookId() == reviewId.getBookId() && Objects.equals(getUserName(), reviewId.getUserName());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getUserName(), getBookId());
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Review review)) return false;
        return getLikes() == review.getLikes() && Objects.equals(getReviewId(), review.getReviewId()) && Objects.equals(getBook(), review.getBook()) && Objects.equals(getUser(), review.getUser()) && Objects.equals(getText(), review.getText()) && getRating() == review.getRating() && Objects.equals(getTime(), review.getTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getReviewId(), getBook(), getUser(), getText(), getRating(), getTime(), getLikes());
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
                ", likes=" + likes +
                '}';
    }
}
