package org.bigBrotherBooks.service;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import org.bigBrotherBooks.configModels.CustomMap;
import org.bigBrotherBooks.dto.AuthorDTO;
import org.bigBrotherBooks.dto.BookDTO;
import org.bigBrotherBooks.dto.UserDTO;
import org.bigBrotherBooks.model.Author;
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

    @Transactional
    public void saveAuthor(Author author){
        authorRepo.persist(author);
    }

    @Transactional
    public Author getAuthor(int authorId){
        return authorRepo.findById((long)authorId);
    }

    @Transactional
    public AuthorDTO getAuthorDTO(int authorId) {
        Author author = getAuthor(authorId);
        if (author == null) {
            return null;
        }
        return mapToAuthorDTO(author);
    }


    @Transactional
    public Author updateAuthor(Author author) {
        Author existingAuthor = getAuthor(author.getAuthorId());
        if (existingAuthor == null) {
            return null;
        }
        mapAuthorDetails(author, existingAuthor);   // transactional operation, so changes in the table without update call
        return author;
    }

    @Transactional
    public boolean deleteAuthor(int authorId){
        Author author = getAuthor(authorId);
        if(author == null){
            return false;
        }
        authorRepo.delete(author);
        return true;
    }

    @Transactional
    public List<Author> getAllAuthors() {
        return authorRepo.listAll();
    }

    @Transactional
    public List<AuthorDTO> getAllAuthorDTOs() {
        List<Author> authors = getAllAuthors();
        return authors.stream().map(AuthorService::mapToAuthorDTO).toList();
    }

    @Transactional
    public int publishBook(int authorId, int bookId) {
        Book book = bookService.getBook(bookId);
        if (book == null) {
            return 0;
        }
        Author author = getAuthor(authorId);
        if (author == null) {
            return -1;
        }
        author.addBook(book);
        return 1;
    }

    @Transactional
    public Object getFullAuthor(int authorId) {
        Author author = getAuthor(authorId);
        if (author == null) {
            return null;
        }
        AuthorDTO authorDTO = mapToAuthorDTO(author);
        List<BookDTO> bookDTOs = author.getBooks().stream().map(BookService::mapToBookDTO).toList();
        List<UserDTO> fanDTOs = author.getFans().stream().map(UserService::mapToUserDTO).toList();
        return CustomMap.of(authorDTO, bookDTOs, fanDTOs);
    }

    public Author getDummyAuthor(){
        Author author = new Author();
        author.setName("Dummy Author");
        author.setAbout("This is a dummy author");
        return author;
    }

    public static AuthorDTO mapToAuthorDTO(Author author) {
        return new AuthorDTO(author.getAuthorId(), author.getName(), author.getAbout());
    }

    private void mapAuthorDetails(Author author, Author existingAuthor) {
        existingAuthor.setName(author.getName());
        existingAuthor.setAbout(author.getAbout());
//        existingAuthor.setBooks(author.getBooks());
//        existingAuthor.setFans(author.getFans());
    }

}
