package org.bigBrotherBooks.service;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import org.bigBrotherBooks.configModels.CustomMap;
import org.bigBrotherBooks.dto.AuthorDTO;
import org.bigBrotherBooks.dto.BookDTO;
import org.bigBrotherBooks.dto.ReviewDTO;
import org.bigBrotherBooks.dto.UserDTO;
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

    public void saveBook(BookDTO bookDTO) {
        Book book = new Book();
        mapToBook(bookDTO, book);
        bookRepo.persist(book);
    }

    public Book getBookById(int bookId) {
        return bookRepo.findById((long)bookId);
    }

    public BookDTO getBookDTO(int bookId) {
        Book book = bookRepo.findById((long) bookId);
        return mapToBookDTO(book);
    }

    @Transactional
    public boolean updateBook(BookDTO bookDTO) {
        Book book = getBookById(bookDTO.getBookId());
        if (book == null) {
            return false;
        }
        mapToBook(bookDTO, book);   // transactional operation, so changes in the table without update call
        return true;
    }

    public boolean deleteBook(int bookId){
        if (getBookById(bookId) == null) {
            return false;
        }
        bookRepo.deleteById((long)bookId);
        return true;
    }

    public List<Book> getBooks(List<Integer> bookIds){
        return bookRepo.findBulk(bookIds);
    }

    @Transactional
    public Object getFullBook(int bookId) {
        Book book = getBookById(bookId);
        if (book == null) {
            return null;
        }
        BookDTO bookDTO = mapToBookDTO(book);
        AuthorDTO authorDTO = AuthorService.mapToAuthorDTO(book.getAuthor());
        List<UserDTO> userDTOs = book.getFans().stream().map(UserService::mapToUserDTO).toList();
        return CustomMap.of(bookDTO, authorDTO, userDTOs);
    }

    public List<BookDTO> getBookDTOs(List<Integer> bookIds) {
        List<Book> books = getBooks(bookIds);
        return books.stream().map(BookService::mapToBookDTO).toList();
    }

    public List<Book> getAllBooks() {
        return bookRepo.listAll();
    }

    public List<BookDTO> getAllBookDTOs() {
        List<Book> books = getAllBooks();
        return books.stream().map(BookService::mapToBookDTO).toList();
    }

    @Transactional
    public List<ReviewDTO> getReviews(int bookId) {
        Book book = getBookById(bookId);
        return book.getReviews().stream().map(ReviewService::mapToReviewDTO).toList();
    }

    public Book getDummyBook(){
        Book book = new Book();
        book.setName("Dummy Book");
        book.setDescription("This is a dummy book");
        return book;
    }

    public static BookDTO mapToBookDTO(Book book) {
        return new BookDTO(book.getBookId(), book.getName(), book.getDescription(), book.getRating(), book.getGenres());
    }

    private void mapToBook(BookDTO bookDTO, Book book) {
        book.setDescription(bookDTO.getDescription());
        book.setName(bookDTO.getName());
        book.setGenres(bookDTO.getGenres());
        book.setRating(bookDTO.getRating());
//        book.setAuthor(bookDTO.getAuthor());
//        book.setFans(bookDTO.getFans());
//        book.setReviews(bookDTO.getReviews());
    }

}
