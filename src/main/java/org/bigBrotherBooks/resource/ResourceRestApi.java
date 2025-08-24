package org.bigBrotherBooks.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bigBrotherBooks.service.ResourceService;

import java.util.Map;

@Path("/resource")
public class ResourceRestApi {

    private final ResourceService resourceService;

    @Inject
    public ResourceRestApi(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @POST
    @Path("/{resourceType}/{resourceId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getResource(@PathParam("resourceType") String resourceType,
                                @PathParam("resourceId") String resourceId,
                                Map<String, Object> inputs) {
        try {
            return Response.ok(resourceService.getResource(resourceType, resourceId, inputs)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error fetching resource: " + e.getMessage()).build();
        }
    }


}
