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

    @ManyToMany(mappedBy = "favoriteBooks")
    private Set<User> fans;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Review> reviews;

    @Column(name = "description")
    private String description;

    @Column(name = "rating")
    private Double rating;

    @Column(name = "genres")
    private List<Genre> genres;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Stock> warehouseStock;

    @Column
    private String language;

    @Column
    private String type;

    @Column
    private String publisher;

    @Column
    private String edition;

    @Column
    private String isbn10;

    @Column
    private String isbn13;

    @Column
    private Integer pages;

    @Column
    private Integer year;

    @Column
    private Double price;

    @Column
    private String coverImage;

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

    public void addReview(Review review) {
        reviews.add(review);
        review.setBook(this);
    }

    public void removeReview(Review review) {
        if (reviews.contains(review)) {
            reviews.remove(review);
            review.setBook(null);
        }
    }

    public Set<Stock> getWarehouseStock() {
        return warehouseStock;
    }

    public void setWarehouseStock(Set<Stock> warehouseStock) {
        this.warehouseStock = warehouseStock;
    }

    public void addWarehouseStock(Stock stock) {
        warehouseStock.add(stock);
        stock.setBook(this);
    }

    public void removeWarehouseStock(Stock stock) {
        if (warehouseStock.contains(stock)) {
            warehouseStock.remove(stock);
            stock.setBook(null);
        }
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
        return "Book{" +
                "bookId=" + bookId +
                ", name='" + name + '\'' +
                ", author=" + author +
                ", fans=" + fans +
                ", reviews=" + reviews +
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
                ", warehouseStock=" + warehouseStock +
                '}';
    }
}
