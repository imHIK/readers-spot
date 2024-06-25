package org.bigBrotherBooks.service;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import org.bigBrotherBooks.dto.BookDTO;
import org.bigBrotherBooks.model.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Singleton
public class BookService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookService.class);

    private BookRepository bookRepo;

    @Inject
    public BookService(BookRepository bookRepo) {
        this.bookRepo = bookRepo;
    }

    @Transactional
    public void saveBook(Book book){
        LOGGER.info("Saving book: {}", book.getName());
        bookRepo.persist(book);
    }

    @Transactional
    public Book getBook(int bookId){
        return bookRepo.findById((long)bookId);
    }

    @Transactional
    public BookDTO getBookDTO(int bookId) {
        Book book = bookRepo.findById((long) bookId);
        return mapToBookDTO(book);
    }

    @Transactional
    public Book updateBook(Book book) {
        Book existingBook = getBook(book.getBookId());
        if (existingBook == null) {
            LOGGER.info("Book not found with id: {}", book.getBookId());
            return null;
        }
        LOGGER.info("Updating book: {}", book.getName());
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

    @Transactional
    public List<Book> getBooks(List<Integer> bookIds){
        return bookRepo.findBulk(bookIds);
    }

    @Transactional
    public List<BookDTO> getBookDTOs(List<Integer> bookIds) {
        List<Book> books = getBooks(bookIds);
        return books.stream().map(this::mapToBookDTO).toList();
    }

    @Transactional
    public List<Book> getAllBooks() {
        return bookRepo.listAll();
    }

    @Transactional
    public List<BookDTO> getAllBookDTOs() {
        List<Book> books = getAllBooks();
        return books.stream().map(this::mapToBookDTO).toList();
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
        existingBook.setFans(book.getFans());
        existingBook.setReviews(book.getReviews());
    }

    private BookDTO mapToBookDTO(Book book) {
        return new BookDTO(book.getBookId(), book.getName(), book.getDescription(), book.getRating(), book.getGenres());
    }

}
