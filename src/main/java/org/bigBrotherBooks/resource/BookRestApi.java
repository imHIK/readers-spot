package org.bigBrotherBooks.resource;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bigBrotherBooks.dto.BookDTO;
import org.bigBrotherBooks.dto.ReviewDTO;
import org.bigBrotherBooks.logger.LogType;
import org.bigBrotherBooks.logger.Logger;
import org.bigBrotherBooks.logger.LoggerFactory;
import org.bigBrotherBooks.service.BookService;

import java.util.List;

@Path("/resource/book")
public class BookRestApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookRestApi.class);
    private final BookService bookService;

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
    public Response saveBook(@Valid BookDTO bookDTO) {
        LOGGER.logThis(LogType.INFO, "Save Book");
        bookService.saveBook(bookDTO);
        return Response.status(Response.Status.CREATED).entity("Book " + bookDTO.getName() + " saved Successfully").build();
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
        LOGGER.logThis(LogType.INFO, "Delete Book");
        if (bookService.deleteBook(bookId))
            return Response.ok("Book " + bookId + " Deleted Successfully").build();
        return Response.status(Response.Status.NOT_FOUND).entity("Book not found").build();
    }

    @PUT
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateBook(@Valid BookDTO bookDTO) {
        LOGGER.logThis(LogType.INFO, "Update Book");
        if (bookService.updateBook(bookDTO)) {
            return Response.ok("Book " + bookDTO.getBookId() + " Updated Successfully").build();
        }
        return Response.status(Response.Status.NOT_FOUND).entity("Book not found").build();
    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllBooks() {
        return Response.ok(bookService.getAllBookDTOs()).build();
    }

    @GET
    @Path("/detailed/{book_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFullBook(@PathParam("book_id") int bookId) {
        Object book = bookService.getFullBook(bookId);
        if (book == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Book " + bookId + " not found").build();
        }
        return Response.ok(book).build();
    }

    @GET
    @Path("/reviews/{book_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBookReviews(@PathParam("book_id") int bookId) {
        List<ReviewDTO> reviews = bookService.getReviews(bookId);
        return Response.ok(reviews).build();
    }

}
