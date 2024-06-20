package org.bigBrotherBooks.service;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.bigBrotherBooks.model.Author;
import org.bigBrotherBooks.model.AuthorDetails;
import org.bigBrotherBooks.model.Book;

import java.util.List;

@Singleton
public class AuthorService {

    private AuthorRepository authorRepo;
    private BookService bookService;

    @Inject
    public AuthorService(AuthorRepository authorRepo, BookService bookService) {
        this.authorRepo = authorRepo;
        this.bookService = bookService;
    }

    public void saveAuthor(Author author){
        authorRepo.persist(author);
    }

    public Author getAuthor(int authorId){
        return authorRepo.findById((long)authorId);
    }

    public AuthorDetails getAuthorDetails(int authorId){
        Author author = authorRepo.findById((long)authorId);
        if (author == null) {
            return null;
        }
        List<Book> books = bookService.getBooks(author.getBooks());
        return new AuthorDetails(author, books);
    }

    public boolean deleteAuthor(int authorId){
        Author author = getAuthor(authorId);
        if(author == null){
            return false;
        }
        authorRepo.delete(author);
        return true;
    }

    public void getAuthors(){
        authorRepo.listAll();
    }

    public List<Book> getBooksByAuthor(int authorId){
        Author author = authorRepo.findById((long)authorId);
        if (author == null) {
            return null;
        }
        List<Integer> bookIds = author.getBooks();
        if(bookIds == null || bookIds.isEmpty()){
            return null;
        }
        return bookService.getBooks(bookIds);
    }

    public Author getDummyAuthor(){
        Author author = new Author();
        author.setName("Dummy Author");
        author.setAbout("This is a dummy author");
        return author;
    }

}
