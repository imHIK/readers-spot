package org.bigBrotherBooks.service;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.bigBrotherBooks.model.Author;
import org.bigBrotherBooks.model.Book;

import java.util.List;

@Singleton
public class AuthorUtils {

    AuthorRepository authorRepo;
    BookUtils bookUtils;

    @Inject
    public AuthorUtils(AuthorRepository authorRepo, BookUtils bookUtils) {
        this.authorRepo = authorRepo;
        this.bookUtils = bookUtils;
    }

    public void saveAuthor(Author author){
        authorRepo.persist(author);
    }

    public Author getAuthor(int authorId){
        return authorRepo.findById((long)authorId);
    }

    public boolean deleteAuthor(int authorId){
        if(authorRepo.findById((long)authorId) == null){
            return false;
        }
        authorRepo.deleteById((long)authorId);
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
        return bookUtils.getBooks(bookIds);
    }

    public Author getDummyAuthor(){
        Author author = new Author();
        author.setName("Dummy Author");
        author.setAbout("This is a dummy author");
        return author;
    }

}
