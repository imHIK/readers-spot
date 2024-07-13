package org.bigBrotherBooks.resource;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bigBrotherBooks.dto.ReviewDTO;
import org.bigBrotherBooks.logger.LogType;
import org.bigBrotherBooks.logger.Logger;
import org.bigBrotherBooks.logger.LoggerFactory;
import org.bigBrotherBooks.service.ReviewService;

@Path("/review")
public class ReviewRestApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReviewRestApi.class);
    private final ReviewService reviewService;

    @Inject
    public ReviewRestApi(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @POST
    @Path("/{user_name}/{book_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addReview(@PathParam("user_name") String userName, @PathParam("book_id") int bookId, @Valid ReviewDTO reviewDTO) {
        LOGGER.logThis(LogType.INFO, "User: {} Added Review for Book: {}", userName, bookId);
        if (reviewService.addReview(userName, bookId, reviewDTO))
            return Response.ok("Review added by " + userName + " for Book " + bookId).build();
        return Response.status(Response.Status.NOT_FOUND).entity("User or Book not found").build();
    }

    @DELETE
    @Path("/{user_name}/{book_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeReview(@PathParam("user_name") String userName, @PathParam("book_id") int bookId) {
        LOGGER.logThis(LogType.INFO, "User: {} Added Review for Book: {}", userName, bookId);
        if (reviewService.removeReview(userName, bookId))
            return Response.ok("Review of book " + bookId + " removed by " + userName).build();
        return Response.status(Response.Status.NOT_FOUND).entity("Review not found").build();
    }

    @PUT
    @Path("/{user_name}/{book_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateReview(@PathParam("user_name") String userName, @PathParam("book_id") int bookId, @Valid ReviewDTO reviewDTO) {
        LOGGER.logThis(LogType.INFO, "User: {} Updated Review for Book: {}", userName, bookId);
        if (reviewService.updateReview(userName, bookId, reviewDTO))
            return Response.ok("Review " + reviewDTO.getReviewId() + " updated by " + userName).build();
        return Response.status(Response.Status.NOT_FOUND).entity("User or Review not found").build();
    }

    @GET
    @Path("/{user_name}/{book_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReview(@PathParam("user_name") String userName, @PathParam("book_id") int bookId) {
        ReviewDTO review = reviewService.getReview(userName, bookId);
        if (review == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Review not found").build();
        }
        return Response.ok(review).build();
    }
}
