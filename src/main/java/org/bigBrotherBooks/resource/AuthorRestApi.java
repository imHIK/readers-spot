package org.bigBrotherBooks.resource;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bigBrotherBooks.dto.AuthorDTO;
import org.bigBrotherBooks.logger.LogType;
import org.bigBrotherBooks.logger.Logger;
import org.bigBrotherBooks.logger.LoggerFactory;
import org.bigBrotherBooks.service.AuthorService;


@Path("/author")
public class AuthorRestApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorRestApi.class);
    private final AuthorService authorService;

    @Inject
    AuthorRestApi(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GET
    @Path("/home")
    @Produces(MediaType.APPLICATION_JSON)
    public Response home() {
        return Response.ok("Welcome to the Authors API").build();
    }

    @POST
    @Path("/save")
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveAuthor(@Valid AuthorDTO authorDTO) {
        LOGGER.logThis(LogType.INFO, "Save Author: {}", () -> authorDTO);
        authorService.saveAuthor(authorDTO);
        return Response.ok("Author " + authorDTO.getName() + " saved Successfully").build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAuthor(@PathParam("id") int authorId) {
        AuthorDTO authorDTO = authorService.getAuthorDTO(authorId);
        return Response.ok(authorDTO).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteAuthor(@PathParam("id") int authorId) {
        LOGGER.logThis(LogType.INFO, "Delete Author: {}", authorId);
        if (authorService.deleteAuthor(authorId))
            return Response.ok("Author " + authorId + " Deleted Successfully").build();
        return Response.status(Response.Status.NOT_FOUND).entity("Author not found").build();
    }

    @PUT
    @Path("/update")                                    // take id explicitly or not
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateAuthor(@Valid AuthorDTO authorDTO) {
        LOGGER.logThis(LogType.INFO, "Update Author: {}", authorDTO.getAuthorId());
        if (authorService.updateAuthor(authorDTO)) {
            return Response.ok("Author " + authorDTO.getAuthorId() + " Updated Successfully").build();
        }
        return Response.status(Response.Status.NOT_FOUND).entity("Author not found").build();
    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllAuthors() {
        return Response.ok(authorService.getAllAuthorDTOs()).build();
    }

    @POST
    @Path("/publish/{author_id}/{book_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response publishBook(@PathParam("author_id") int authorId, @PathParam("book_id") int bookId) {
        LOGGER.logThis(LogType.INFO, "Publish Book: {} by Author: {}", bookId, authorId);
        switch (authorService.publishBook(authorId, bookId)) {
            case 1:
                return Response.ok("Book " + bookId + " published by Author " + authorId).build();
            case 0:
                return Response.status(Response.Status.NOT_FOUND).entity("Book not found, please add the book entry first").build();
            case -1:
                return Response.status(Response.Status.NOT_FOUND).entity("Author not found, please add the author entry first").build();
            default:
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Unknown error").build();
        }
    }

    @GET
    @Path("/detailed/{author_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDetailedAuthor(@PathParam("author_id") int authorId) {
        Object author = authorService.getFullAuthor(authorId);
        if (author == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Author " + authorId + " not found").build();
        }
        return Response.ok(author).build();
    }
}
