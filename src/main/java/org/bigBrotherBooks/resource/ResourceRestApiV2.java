package org.bigBrotherBooks.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bigBrotherBooks.api.HttpServiceV2;
import org.bigBrotherBooks.configModels.GoogleBooksVolumeInfo;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Path("/resource")
public class ResourceRestApiV2 {

    private final HttpServiceV2 httpService;
    @ConfigProperty(name = "google-cloud-api-key")
    private String key;

    @Inject
    public ResourceRestApiV2(HttpServiceV2 httpService) {
        this.httpService = httpService;
    }

    @GET
    @Path("/book/{isbn}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBook(@PathParam("isbn") String isbn) {
        try {
            GoogleBooksVolumeInfo booksInfo = httpService.getBookById(isbn, key);
            return Response.ok(booksInfo).build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
