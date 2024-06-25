package org.bigBrotherBooks.dto;

import org.bigBrotherBooks.configModels.Genre;

import java.util.List;

public class BookDTO {

    private int bookId;
    private String name;
    private String description;
    private Double rating;
    private List<Genre> genres;

    public BookDTO() {
    }

    public BookDTO(int bookId, String name, String description, Double rating, List<Genre> genres) {
        this.bookId = bookId;
        this.name = name;
        this.description = description;
        this.rating = rating;
        this.genres = genres;
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

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    @Override
    public String toString() {
        return "BookDTO{" +
                "bookId=" + bookId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", rating=" + rating +
                ", genres=" + genres +
                '}';
    }
}
