package org.bigBrotherBooks.model;

public class BookDetails {
    Book book;
    Author author;

    public BookDetails(Book book, Author author) {
        this.book = book;
        this.author = author;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "BookDetails{" +
                "book=" + book +
                ", author=" + author +
                '}';
    }
}
