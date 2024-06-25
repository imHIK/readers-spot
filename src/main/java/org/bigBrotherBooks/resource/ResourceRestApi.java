package org.bigBrotherBooks.resource;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import org.bigBrotherBooks.dto.AuthorDTO;
import org.bigBrotherBooks.dto.BookDTO;
import org.bigBrotherBooks.model.Author;
import org.bigBrotherBooks.model.Book;
import org.bigBrotherBooks.service.AuthorService;
import org.bigBrotherBooks.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Deprecated
@Path("/deprecated/resource")
public class ResourceRestApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceRestApi.class);
    private AuthorService authorService;
    private BookService bookService;

    @Inject
    ResourceRestApi(AuthorService authorService, BookService bookService) {
        this.authorService = authorService;
        this.bookService = bookService;
    }

    @GET
    @Path("/home")
    @Produces(MediaType.APPLICATION_JSON)
    public Response home() {
        return Response.ok("Welcome to the Resource API").build();
    }

    // author endpoints

    @GET
    @Path("/save/author")
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveAuthor(@Valid Author author) {
        LOGGER.info("Save Author");
        if (author == null) {
            authorService.saveAuthor(authorService.getDummyAuthor());
            return Response.status(Status.ACCEPTED).entity("Author not passed, Dummy Author saved").build();
        }
        authorService.saveAuthor(author);
        return Response.status(Status.CREATED).entity("Author " + author.getAuthorId() + " saved Successfully").build();
    }

    @GET
    @Path("/author/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAuthor(@PathParam("id") int authorId) {
        AuthorDTO authorDTO = authorService.getAuthorDTO(authorId);
        return Response.ok(authorDTO).build();
    }

    @DELETE
    @Path("/author/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteAuthor(@PathParam("id") int authorId) {
        LOGGER.info("Delete Author");
        if (authorService.deleteAuthor(authorId))
            return Response.ok("Author " + authorId + " Deleted Successfully").build();
        return Response.status(Status.NOT_FOUND).entity("Author not found").build();
    }

    // book endpoints

    @POST
    @Path("/save/book")
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveBook(Book book) {
        LOGGER.info("Save Book");
        if (book == null) {
            bookService.saveBook(bookService.getDummyBook());
            return Response.status(Status.ACCEPTED).entity("Book not passed, Dummy Book saved").build();
        }
        bookService.saveBook(book);
        return Response.status(Status.CREATED).entity("Book " + book.getBookId() + " saved Successfully").build();
    }

    @GET
    @Path("/book/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBook(@PathParam("id") int bookId) {
        BookDTO bookDTO = bookService.getBookDTO(bookId);
        return Response.ok(bookDTO).build();
    }

    @DELETE
    @Path("/book/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteBook(@PathParam("id") int bookId) {
        LOGGER.info("Delete Book");
        if (bookService.deleteBook(bookId))
            return Response.ok("Book " + bookId + " Deleted Successfully").build();
        return Response.status(Status.NOT_FOUND).entity("Book not found").build();
    }
}
