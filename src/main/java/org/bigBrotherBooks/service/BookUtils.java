package org.bigBrotherBooks.service;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.bigBrotherBooks.model.Author;
import org.bigBrotherBooks.model.Book;
import org.bigBrotherBooks.model.BookDetails;

import java.util.List;

@Singleton
public class BookUtils {

    BookRepository bookRepo;
    AuthorUtils authorUtils;

    @Inject
    public BookUtils(BookRepository bookRepo, AuthorUtils authorUtils) {
        this.bookRepo = bookRepo;
        this.authorUtils = authorUtils;
    }

    public void saveBook(Book book){
        bookRepo.persist(book);
    }

    public Book getBook(int bookId){
        return bookRepo.findById((long)bookId);
    }

    public BookDetails getBookDetails(int bookId){
        Book book = bookRepo.findById((long)bookId);
        if (book == null) {
            return null;
        }
        Author author = authorUtils.getAuthor(book.getAuthorId());
        return new BookDetails(book, author);
    }

    public boolean deleteBook(int bookId){
        if(bookRepo.findById((long)bookId) == null){
            return false;
        }
        bookRepo.deleteById((long)bookId);
        return true;
    }

    public List<Book> getBooks(List<Integer> bookIds){
        return bookRepo.findBulk(bookIds);
    }

    public void getAllBooks(){
        bookRepo.listAll();
    }

    public Book getDummyBook(){
        Book book = new Book();
        book.setName("Dummy Book");
        book.setAuthorId(123);
        book.setDescription("This is a dummy book");
        return book;
    }

}
