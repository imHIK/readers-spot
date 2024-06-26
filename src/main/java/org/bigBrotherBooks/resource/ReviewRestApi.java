package org.bigBrotherBooks.resource;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bigBrotherBooks.dto.ReviewDTO;
import org.bigBrotherBooks.model.Review;
import org.bigBrotherBooks.service.ReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/review")
public class ReviewRestApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReviewRestApi.class);
    private ReviewService reviewService;

    @Inject
    public ReviewRestApi(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @POST
    @Path("/{user_name}/{book_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addReview(@PathParam("user_name") String userName, @PathParam("book_id") int bookId, @Valid Review review) {
        LOGGER.info("Add Review");
        if (reviewService.addReview(userName, bookId, review))
            return Response.ok("Review added by " + userName + " for Book " + bookId).build();
        return Response.status(Response.Status.NOT_FOUND).entity("User or Book not found").build();
    }

    @DELETE
    @Path("/{user_name}/{book_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeReview(@PathParam("user_name") String userName, @PathParam("book_id") int bookId) {
        LOGGER.info("Remove Review");
        if (reviewService.removeReview(userName, bookId))
            return Response.ok("Review of book " + bookId + " removed by " + userName).build();
        return Response.status(Response.Status.NOT_FOUND).entity("Review not found").build();
    }

    @PUT
    @Path("/{user_name}/{book_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateReview(@PathParam("user_name") String userName, @PathParam("book_id") int bookId, @Valid Review review) {
        LOGGER.info("Update Review");
        if (reviewService.updateReview(userName, bookId, review))
            return Response.ok("Review " + review.getReviewId() + " updated by " + userName).build();
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
