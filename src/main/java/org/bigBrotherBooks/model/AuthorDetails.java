package org.bigBrotherBooks.model;

import java.util.List;

public class AuthorDetails {

    Author author;
    List<Book> books;

    public AuthorDetails(Author author, List<Book> books) {
        this.author = author;
        this.books = books;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    @Override
    public String toString() {
        return "AuthorDetails{" +
                "author=" + author +
                ", books=" + books +
                '}';
    }
}
