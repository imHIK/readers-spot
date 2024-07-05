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
import org.bigBrotherBooks.repository.AuthorRepository;

import java.util.List;

@Singleton
public class AuthorService {

    private final AuthorRepository authorRepo;
    private final BookService bookService;

    @Inject
    public AuthorService(AuthorRepository authorRepo, BookService bookService) {
        this.authorRepo = authorRepo;
        this.bookService = bookService;
    }

    public void saveAuthor(AuthorDTO authorDTO) {
        Author author = new Author();
        mapToAuthor(authorDTO, author);
        authorRepo.persist(author);
    }

    public Author getAuthor(int authorId){
        return authorRepo.findById((long)authorId);
    }

    public AuthorDTO getAuthorDTO(int authorId) {
        Author author = getAuthor(authorId);
        if (author == null) {
            return null;
        }
        return mapToAuthorDTO(author);
    }

    @Transactional
    public boolean updateAuthor(AuthorDTO authorDTO) {
        Author author = getAuthor(authorDTO.getAuthorId());
        if (author == null) {
            return false;
        }
        mapToAuthor(authorDTO, author);   // transactional operation, so changes in the table without update call
        return true;
    }

    public boolean deleteAuthor(int authorId){
        Author author = getAuthor(authorId);
        if(author == null){
            return false;
        }
        authorRepo.deleteById((long) authorId);
        return true;
    }

    public List<Author> getAllAuthors() {
        return authorRepo.listAll();
    }

    public List<AuthorDTO> getAllAuthorDTOs() {
        List<Author> authors = getAllAuthors();
        return authors.stream().map(AuthorService::mapToAuthorDTO).toList();
    }

    @Transactional
    public int publishBook(int authorId, int bookId) {
        Book book = bookService.getBookById(bookId);
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

    private static void mapToAuthor(AuthorDTO authorDTO, Author author) {
        author.setName(authorDTO.getName());
        author.setAbout(authorDTO.getAbout());
//        author.setBooks(authorDTO.getBooks());
//        author.setFans(authorDTO.getFans());
    }

}
