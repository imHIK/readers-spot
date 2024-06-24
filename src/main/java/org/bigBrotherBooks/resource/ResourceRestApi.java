package org.bigBrotherBooks.resource;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import org.bigBrotherBooks.model.Author;
import org.bigBrotherBooks.model.Book;
import org.bigBrotherBooks.service.AuthorService;
import org.bigBrotherBooks.service.BookService;

@Path("/resource")
public class ResourceRestApi {

    private AuthorService authorService;
    private BookService bookService;

    @Inject
    ResourceRestApi(AuthorService authorService, BookService bookService) {
        this.authorService = authorService;
        this.bookService = bookService;
    }

    // author endpoints

    @GET
    @Path("/save/author")
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveAuthor(@Valid Author author) {
        if (author == null) {
            authorService.saveAuthor(authorService.getDummyAuthor());
            return Response.status(Status.ACCEPTED).entity("Author not passed, Dummy Author saved").build();
        }
        authorService.saveAuthor(author);
        return Response.status(Status.CREATED).entity("Author " + author.getAuthorId() + " saved Successfully").build();
    }

    @GET
    @Path("/get/author/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAuthor(@PathParam("id") int authorId) {
        Author author = authorService.getAuthor(authorId);
        return Response.ok(author).build();
    }

    @DELETE
    @Path("/delete/author/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteAuthor(@PathParam("id") int authorId) {
        if (authorService.deleteAuthor(authorId))
            return Response.ok("Author " + authorId + " Deleted Successfully").build();
        return Response.status(Status.NOT_FOUND).entity("Author not found").build();
    }

    // book endpoints

    @GET
    @Path("/save/book")
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveBook(Book book) {
        if (book == null) {
            bookService.saveBook(bookService.getDummyBook());
            return Response.status(Status.ACCEPTED).entity("Book not passed, Dummy Book saved").build();
        }
        bookService.saveBook(book);
        return Response.status(Status.CREATED).entity("Book " + book.getBookId() + " saved Successfully").build();
    }

    @GET
    @Path("/get/book/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBook(@PathParam("id") int bookId) {
        Book book = bookService.getBook(bookId);
        return Response.ok(book).build();
    }

    @DELETE
    @Path("/delete/book/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteBook(@PathParam("id") int bookId) {
        if (bookService.deleteBook(bookId))
            return Response.ok("Book " + bookId + " Deleted Successfully").build();
        return Response.status(Status.NOT_FOUND).entity("Book not found").build();
    }
}
