package org.bigBrotherBooks.resource;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bigBrotherBooks.dto.BookDTO;
import org.bigBrotherBooks.model.Book;
import org.bigBrotherBooks.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/resource/book")
public class BookRestApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookRestApi.class);
    private BookService bookService;

    @Inject
    BookRestApi(BookService bookService) {
        this.bookService = bookService;
    }

    @GET
    @Path("/home")
    @Produces(MediaType.APPLICATION_JSON)
    public Response home() {
        return Response.ok("Welcome to the Books API").build();
    }

    @POST
    @Path("/save")
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveBook(@Valid Book book) {
        LOGGER.info("Save Book");
        if (book == null) {
            bookService.saveBook(bookService.getDummyBook());
            return Response.status(Response.Status.ACCEPTED).entity("Book not passed, Dummy Book saved").build();
        }
        bookService.saveBook(book);
        return Response.status(Response.Status.CREATED).entity("Book " + book.getBookId() + " saved Successfully").build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBook(@PathParam("id") int bookId) {
        BookDTO bookDTO = bookService.getBookDTO(bookId);
        return Response.ok(bookDTO).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteBook(@PathParam("id") int bookId) {
        LOGGER.info("Delete Book");
        if (bookService.deleteBook(bookId))
            return Response.ok("Book " + bookId + " Deleted Successfully").build();
        return Response.status(Response.Status.NOT_FOUND).entity("Book not found").build();
    }

    @PUT
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateBook(@Valid Book book) {
        LOGGER.info("Update Book");
        if (book == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Book not passed").build();
        }
        Book updatedBook = bookService.updateBook(book);
        if (updatedBook == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Book not found").build();
        }
        return Response.ok("Book " + book.getBookId() + " Updated Successfully").build();
    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllBooks() {
        return Response.ok(bookService.getAllBookDTOs()).build();
    }

}
