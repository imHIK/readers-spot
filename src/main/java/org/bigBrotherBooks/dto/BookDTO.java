package org.bigBrotherBooks.dto;

import org.bigBrotherBooks.configModels.Genre;

import java.util.List;

public class BookDTO {

    private int bookId;
    private String name;
    private String description;
    private Double rating;
    private List<Genre> genres;
    private String language;
    private String type;
    private String publisher;
    private String edition;
    private String isbn10;
    private String isbn13;
    private Integer pages;
    private Integer year;
    private Double price;
    private String coverImage;

    public BookDTO() {
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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public String getIsbn10() {
        return isbn10;
    }

    public void setIsbn10(String isbn10) {
        this.isbn10 = isbn10;
    }

    public String getIsbn13() {
        return isbn13;
    }

    public void setIsbn13(String isbn13) {
        this.isbn13 = isbn13;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    @Override
    public String toString() {
        return "BookDTO{" +
                "bookId=" + bookId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", rating=" + rating +
                ", genres=" + genres +
                ", language='" + language + '\'' +
                ", type='" + type + '\'' +
                ", publisher='" + publisher + '\'' +
                ", edition='" + edition + '\'' +
                ", isbn10='" + isbn10 + '\'' +
                ", isbn13='" + isbn13 + '\'' +
                ", pages='" + pages + '\'' +
                ", year='" + year + '\'' +
                ", price='" + price + '\'' +
                ", coverImage='" + coverImage + '\'' +
                '}';
    }
}
