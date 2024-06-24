package org.bigBrotherBooks.service;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import org.bigBrotherBooks.model.Book;

import java.util.List;

@Singleton
public class BookService {

    private BookRepository bookRepo;

    @Inject
    public BookService(BookRepository bookRepo) {
        this.bookRepo = bookRepo;
    }

    @Transactional
    public void saveBook(Book book){
        bookRepo.persist(book);
    }

    public Book getBook(int bookId){
        return bookRepo.findById((long)bookId);
    }

    @Transactional
    public Book updateBook(Book book) {
        Book existingBook = getBook(book.getBookId());
        if (existingBook == null) {
            return null;
        }
        mapBookDetails(book, existingBook);   // transactional operation, so changes in the table without update call
        return book;
    }

    @Transactional
    public boolean deleteBook(int bookId){
        if (getBook(bookId) == null) {
            return false;
        }
        bookRepo.deleteById((long)bookId);
        return true;
    }

    public List<Book> getBooks(List<Integer> bookIds){
        return bookRepo.findBulk(bookIds);
    }

    public List<Book> getAllBooks() {
        return bookRepo.listAll();
    }

    public Book getDummyBook(){
        Book book = new Book();
        book.setName("Dummy Book");
        book.setDescription("This is a dummy book");
        return book;
    }

    private void mapBookDetails(Book book, Book existingBook) {
        existingBook.setDescription(book.getDescription());
        existingBook.setName(book.getName());
        existingBook.setAuthor(book.getAuthor());
        existingBook.setGenres(book.getGenres());
        existingBook.setRating(book.getRating());
        existingBook.setWarehouseStocks(book.getWarehouseStocks());
        existingBook.setFans(book.getFans());
        existingBook.setReviews(book.getReviews());
    }

}


// explain me about using the hibernate annotations in java for relational mapping
// Hibernate is an ORM (Object Relational Mapping) tool that maps Java objects to database tables and vice versa.
// Hibernate provides a set of annotations that can be used to define the mapping between Java objects and database tables.
// Here are some of the commonly used Hibernate annotations for relational mapping:
// @Entity: This annotation is used to mark a Java class as an entity. An entity is a class that maps to a database table.
// @Table: This annotation is used to specify the name of the database table that the entity maps to.
// @Id: This annotation is used to mark a field as the primary key of the entity.
// @GeneratedValue: This annotation is used to specify the generation strategy for the primary key field.
// @Column: This annotation is used to specify the mapping of a field to a database column.
// @ManyToOne: This annotation is used to specify a many-to-one relationship between two entities.
// @OneToMany: This annotation is used to specify a one-to-many relationship between two entities.
// @ManyToMany: This annotation is used to specify a many-to-many relationship between two entities.
// @JoinColumn: This annotation is used to specify the mapping of a foreign key column in a many-to-one or one-to-one relationship.
// @JoinTable: This annotation is used to specify the mapping of a join table in a many-to-many relationship.
// @MapsId: This annotation is used to specify the mapping of a foreign key column in a one-to-one relationship.
// @MapsId: This annotation is used to specify the mapping of a foreign key column in a one-to-one relationship.
// These annotations provide a powerful and flexible way to define the mapping between Java objects and database tables in Hibernate.
