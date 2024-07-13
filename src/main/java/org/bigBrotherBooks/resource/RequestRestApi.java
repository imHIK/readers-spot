package org.bigBrotherBooks.resource;

import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bigBrotherBooks.configModels.BookCondition;
import org.bigBrotherBooks.dto.RentRequestDTO;
import org.bigBrotherBooks.service.RequestService;

@Path("/rent")
public class RequestRestApi {

    private final RequestService requestService;

    public RequestRestApi(RequestService requestService) {
        this.requestService = requestService;
    }

    @POST
    @Path("/request/{user_name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response makeRequest(@PathParam("user_name") String userName, @Valid RentRequestDTO rentRequestDTO) {
        if (requestService.makeRequest(userName, rentRequestDTO)) {
            return Response.ok("Request made successfully").build();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("Error while making request").build();
    }

    @POST
    @Path("/process/{request_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response processRequest(@PathParam("request_id") int requestId) {
        if (requestService.processRequest(requestId)) {
            return Response.ok("Request processed successfully").build();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("Error while processing request").build();
    }

    @POST
    @Path("/return/{request_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response processReturn(@PathParam("request_id") int requestId, @Valid BookCondition returnCondition) {
        if (requestService.processReturn(requestId, returnCondition)) {
            return Response.ok("Books returned successfully").build();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("Error while returning Book").build();
    }

    @GET
    @Path("/{request_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRequest(@PathParam("request_id") int requestId) {
        RentRequestDTO rentRequestDTO = requestService.getRentRequestDTO(requestId);
        if (rentRequestDTO != null) {
            return Response.ok(rentRequestDTO).build();
        }
        return Response.status(Response.Status.NOT_FOUND).entity("Request not found").build();
    }

    @DELETE
    @Path("/{request_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteRequest(@PathParam("request_id") int requestId) {
        if (requestService.removeRequest(requestId)) {
            return Response.ok("Request deleted successfully").build();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("Error while deleting request").build();
    }

    @PUT
    @Path("/{request_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateRequest(@PathParam("request_id") int requestId, @Valid RentRequestDTO rentRequestDTO) {
        rentRequestDTO.setReqId(requestId);
        if (requestService.updateRequest(rentRequestDTO)) {
            return Response.ok("Request updated successfully").build();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity("Error while updating request").build();
    }

}
