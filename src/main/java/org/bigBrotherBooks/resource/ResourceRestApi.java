package org.bigBrotherBooks.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bigBrotherBooks.model.Author;
import org.bigBrotherBooks.model.Book;
import org.bigBrotherBooks.service.AuthorUtils;
import org.bigBrotherBooks.service.BookUtils;

@Path("/resource")
public class ResourceRestApi {

    private AuthorUtils authorUtils;
    private BookUtils bookUtils;

    @Inject
    ResourceRestApi( AuthorUtils authorUtils, BookUtils bookUtils){
        this.authorUtils = authorUtils;
        this.bookUtils = bookUtils;
    }

    // author endpoints

    @GET
    @Path("/save/author")
    @Produces(MediaType.TEXT_PLAIN)
    public String saveAuthor() {
        Author author = authorUtils.getDummyAuthor();
        authorUtils.saveAuthor(author);
        return "Author Saved Successfully";
    }

    @GET
    @Path("/get/author/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getAuthor(@PathParam("id") int authorId) {
        Author author = authorUtils.getAuthor(authorId);
        return Response.ok(author).build();
    }

    @DELETE
    @Path("/delete/author/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public String deleteAuthor(@PathParam("id") int authorId) {
        if(authorUtils.deleteAuthor(authorId))
            return "Author Deleted Successfully";
        return "Author not found";
    }

    // book endpoints

    @GET
    @Path("/save/book")
    @Produces(MediaType.TEXT_PLAIN)
    public String saveBook() {
        Book book = bookUtils.getDummyBook();
        bookUtils.saveBook(book);
        return "Book Saved Successfully";
    }

    @GET
    @Path("/get/book/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getBook(@PathParam("id") int bookId) {
        Book book = bookUtils.getBook(bookId);
        return Response.ok(book).build();
    }

    @DELETE
    @Path("/delete/book/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public String deleteBook(@PathParam("id") int bookId) {
        if (bookUtils.deleteBook(bookId))
            return "Book Deleted Successfully";
        return "Book not found";
    }
}
