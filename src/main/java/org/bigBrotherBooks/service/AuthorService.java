package org.bigBrotherBooks.service;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import org.bigBrotherBooks.dto.AuthorDTO;
import org.bigBrotherBooks.model.Author;

import java.util.List;

@Singleton
public class AuthorService {

    private AuthorRepository authorRepo;

    @Inject
    public AuthorService(AuthorRepository authorRepo) {
        this.authorRepo = authorRepo;
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

    public List<Author> getAuthors() {
        return authorRepo.listAll();
    }

    public Author getDummyAuthor(){
        Author author = new Author();
        author.setName("Dummy Author");
        author.setAbout("This is a dummy author");
        return author;
    }

    private void mapAuthorDetails(Author author, Author existingAuthor) {
        existingAuthor.setAbout(author.getAbout());
        existingAuthor.setName(author.getName());
        existingAuthor.setBooks(author.getBooks());
        existingAuthor.setFans(author.getFans());
    }

    AuthorDTO mapToAuthorDTO(Author author) {
        return new AuthorDTO(author.getAuthorId(), author.getName(), author.getAbout());
    }

}
