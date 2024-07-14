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
import org.bigBrotherBooks.repository.BookRepository;

import java.util.List;

@Singleton
public class BookService {

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

    public static BookDTO mapToBookDTO(Book book) {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setBookId(book.getBookId());
        bookDTO.setName(book.getName());
        bookDTO.setDescription(book.getDescription());
        bookDTO.setRating(book.getRating());
        bookDTO.setGenres(book.getGenres());
        bookDTO.setIsbn10(book.getIsbn10());
        bookDTO.setIsbn13(book.getIsbn13());
        bookDTO.setLanguage(book.getLanguage());
        bookDTO.setType(book.getType());
        bookDTO.setPrice(book.getPrice());
        bookDTO.setPages(book.getPages());
        bookDTO.setPublisher(book.getPublisher());
        bookDTO.setCoverImage(book.getCoverImage());
        bookDTO.setYear(book.getYear());
        bookDTO.setEdition(book.getEdition());
        return bookDTO;

    }

    private void mapToBook(BookDTO bookDTO, Book book) {
        book.setDescription(bookDTO.getDescription());
        book.setName(bookDTO.getName());
        book.setGenres(bookDTO.getGenres());
        book.setRating(bookDTO.getRating());
        book.setLanguage(bookDTO.getLanguage());
        book.setType(bookDTO.getType());
        book.setPrice(bookDTO.getPrice());
        book.setPages(bookDTO.getPages());
        book.setPublisher(bookDTO.getPublisher());
        book.setCoverImage(bookDTO.getCoverImage());
        book.setYear(bookDTO.getYear());
        book.setEdition(bookDTO.getEdition());
        book.setIsbn10(bookDTO.getIsbn10());
        book.setIsbn13(bookDTO.getIsbn13());
        book.setBookId(bookDTO.getBookId());
//        book.setAuthor(bookDTO.getAuthor());
//        book.setFans(bookDTO.getFans());
//        book.setReviews(bookDTO.getReviews());
    }

}
