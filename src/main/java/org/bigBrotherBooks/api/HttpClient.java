package org.bigBrotherBooks.api;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import org.bigBrotherBooks.constants.GlobalConstants;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(baseUri = GlobalConstants.BOOKS_API_BASE_URL)
public interface HttpClient {

    @GET
    @Path("/volumes")
    String getBooks(@QueryParam("q") String query, @QueryParam("key") String key);

}
