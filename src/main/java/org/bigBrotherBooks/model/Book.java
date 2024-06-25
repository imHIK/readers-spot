package org.bigBrotherBooks.model;

import jakarta.persistence.*;
import org.bigBrotherBooks.configModels.Genre;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a unique book entry.
 * Fields
 * bookId: int
 * name: String
 * author: Author -> many to one
 * fans: Set<User> -> many to many
 * reviews: Set<Review> -> one to many
 * description: String
 * rating: Double
 * genres: List<Genre>
 */

@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private int bookId;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    @ManyToMany(mappedBy = "favoriteBooks", fetch = FetchType.LAZY)
    private Set<User> fans;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Review> reviews;

    @Column(name = "description")
    private String description;

    @Column(name = "rating")
    private Double rating;

    @Column(name = "genres")
    private List<Genre> genres;

    public Book() {
        fans = new HashSet<>();
        reviews = new HashSet<>();
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Set<User> getFans() {
        return fans;
    }

    public void setFans(Set<User> fans) {
        this.fans = fans;
    }

    public void addFan(User user) {
        fans.add(user);
        user.getFavoriteBooks().add(this);
    }

    public void removeFan(User user) {
        fans.remove(user);
        user.getFavoriteBooks().remove(this);
    }

    public Set<Review> getReviews() {
        return reviews;
    }

    public void setReviews(Set<Review> reviews) {
        this.reviews = reviews;
    }


    @Override
    public String toString() {
        return "Book{" +
                "bookId=" + bookId +
                ", name='" + name + '\'' +
                ", author=" + author +
                ", fans=" + fans +
                ", reviews=" + reviews +
                ", description='" + description + '\'' +
                ", rating=" + rating +
                ", genres=" + genres +
                '}';
    }
}
