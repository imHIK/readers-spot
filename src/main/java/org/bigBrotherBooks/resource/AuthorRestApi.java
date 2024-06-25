package org.bigBrotherBooks.resource;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bigBrotherBooks.dto.AuthorDTO;
import org.bigBrotherBooks.model.Author;
import org.bigBrotherBooks.service.AuthorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Path("/resource/author")
public class AuthorRestApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorRestApi.class);
    private AuthorService authorService;

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
    public Response saveAuthor(@Valid Author author) {
        LOGGER.info("Save Author");
        if (author == null) {
            authorService.saveAuthor(authorService.getDummyAuthor());
            return Response.status(Response.Status.ACCEPTED).entity("Author not passed, Dummy Author saved").build();
        }
        authorService.saveAuthor(author);
        return Response.status(Response.Status.CREATED).entity("Author " + author.getAuthorId() + " saved Successfully").build();
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
        LOGGER.info("Delete Author");
        if (authorService.deleteAuthor(authorId))
            return Response.ok("Author " + authorId + " Deleted Successfully").build();
        return Response.status(Response.Status.NOT_FOUND).entity("Author not found").build();
    }

    @PUT
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateAuthor(@Valid Author author) {
        LOGGER.info("Update Author");
        if (author == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Author not passed").build();
        }
        Author updatedAuthor = authorService.updateAuthor(author);
        if (updatedAuthor == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Author not found").build();
        }
        return Response.ok("Author " + author.getAuthorId() + " Updated Successfully").build();
    }

}
