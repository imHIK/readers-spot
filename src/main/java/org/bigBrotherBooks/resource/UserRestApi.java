package org.bigBrotherBooks.resource;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bigBrotherBooks.dto.UserDTO;
import org.bigBrotherBooks.dto.UserProfileUpdateDTO;
import org.bigBrotherBooks.model.Review;
import org.bigBrotherBooks.model.User;
import org.bigBrotherBooks.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Path("/user")
public class UserRestApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserRestApi.class);
    private UserService userService;

    @Inject
    public UserRestApi(UserService userService) {
        this.userService = userService;
    }

    @GET
    @Path("/home")
    @Produces(MediaType.APPLICATION_JSON)
    public Response home() {
        return Response.ok("Welcome to the User API").build();
    }

    @GET
    @Path("/{user_name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("user_name") String userName) {
        UserDTO user = userService.getUserDTO(userName);
if(user == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("User " + userName + " not found").build();
        }

        return Response.ok(user).build();
    }

    @POST
    @Path("/save")
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveUser(@Valid User user) {
        LOGGER.info("Save User");
        userService.saveUser(user);
        return Response.status(Response.Status.CREATED).entity("User " + user.getUserName() + " saved successfully").build();
    }

    @POST
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(@Valid User user) {
        LOGGER.info("Update User");
        if (userService.updateUser(user)) {
            return Response.ok("User " + user.getUserName() + " updated successfully").build();
        }
        return Response.status(Response.Status.NOT_FOUND).entity("User " + user.getUserName() + " not found").build();
    }

    @DELETE      // TODO: hard and soft delete
    @Path("/{user_name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUser(@PathParam("user_name") String userName) {
        LOGGER.info("Delete User");
        User user = userService.getUserById(userName);
        if(user == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("User " + userName + " not found").build();
        }
        userService.deleteUser(userName);
        return Response.ok("User " + userName + " deleted successfully").build();
    }

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers(@QueryParam("user_names") List<String> userNames) {
        List<UserDTO> users = userService.getUserDTOs(userNames);
        return Response.ok(users).build();
    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsers() {
        List<UserDTO> users = userService.getAllUserDTOs();
        return Response.ok(users).build();
    }

    @POST
    @Path("/follow/{user_name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response followUser(@PathParam("user_name") String toUserName, @QueryParam("from") String fromUserName) {
        LOGGER.info("Follow User");
        if (userService.followUser(fromUserName, toUserName, true))
            return Response.ok("User " + fromUserName + " is now following " + toUserName).build();
        return Response.status(Response.Status.NOT_FOUND).entity("User or Followed User not found").build();
    }

    @POST
    @Path("/unfollow/{user_name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response unfollowUser(@PathParam("user_name") String toUserName, @QueryParam("from") String fromUserName) {
        LOGGER.info("Unfollow User");
        if (userService.followUser(fromUserName, toUserName, false))
            return Response.ok("User " + fromUserName + " has unfollowed " + toUserName).build();
        return Response.ok("User " + fromUserName + " has unfollowed " + toUserName).build();
    }

    @POST
    @Path("/updateProfile")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateProfile(@Valid UserProfileUpdateDTO userProfileUpdateDTO) {
        LOGGER.info("Update Profile");
        userService.updateProfile(userProfileUpdateDTO);
        return Response.ok("Profile updated successfully").build();
    }

    @GET
    @Path("/detailed/{user_name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFullUser(@PathParam("user_name") String userName) {
        Object user = userService.getFullUser(userName);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("User " + userName + " not found").build();
        }
        return Response.ok(user).build();
    }

    @POST
    @Path("/addFavoriteBook/{user_name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addFavoriteBook(@PathParam("user_name") String userName, @QueryParam("book_id") int bookId) {
        LOGGER.info("Add Favorite Book");
        if (userService.modifyFavoriteBook(userName, bookId, true))
            return Response.ok("Book " + bookId + " added to favorites of " + userName).build();
        return Response.status(Response.Status.NOT_FOUND).entity("User or Book not found").build();
    }

    @POST
    @Path("/removeFavoriteBook/{user_name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeFavoriteBook(@PathParam("user_name") String userName, @QueryParam("book_id") int bookId) {
        LOGGER.info("Remove Favorite Book");
        if (userService.modifyFavoriteBook(userName, bookId, false))
            return Response.ok("Book " + bookId + " removed from favorites of " + userName).build();
        return Response.status(Response.Status.NOT_FOUND).entity("User or Book not found").build();
    }

    @POST
    @Path("/addFavoriteAuthor/{user_name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addFavoriteAuthor(@PathParam("user_name") String userName, @QueryParam("author_id") int authorId) {
        LOGGER.info("Add Favorite Author");
        if (userService.modifyFavoriteAuthor(userName, authorId, true))
            return Response.ok("Author " + authorId + " added to favorites of " + userName).build();
        return Response.status(Response.Status.NOT_FOUND).entity("User or Author not found").build();
    }

    @POST
    @Path("/removeFavoriteAuthor/{user_name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeFavoriteAuthor(@PathParam("user_name") String userName, @QueryParam("author_id") int authorId) {
        LOGGER.info("Remove Favorite Author");
        if (userService.modifyFavoriteAuthor(userName, authorId, false))
            return Response.ok("Author " + authorId + " removed from favorites of " + userName).build();
        return Response.status(Response.Status.NOT_FOUND).entity("User or Author not found").build();
    }

    @POST
    @Path("/addReview/{user_name}/{book_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addReview(@PathParam("user_name") String userName, @PathParam("book_id") int bookId, @Valid Review review) {
        LOGGER.info("Add Review");
        if (userService.addReview(userName, bookId, review))
            return Response.ok("Review added by " + userName + " for Book " + bookId).build();
        return Response.status(Response.Status.NOT_FOUND).entity("User or Book not found").build();
    }

    @POST
    @Path("/removeReview/{user_name}/{review_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeReview(@PathParam("user_name") String userName, @PathParam("review_id") int reviewId) {
        LOGGER.info("Remove Review");
        if (userService.removeReview(userName, reviewId))
            return Response.ok("Review " + reviewId + " removed by " + userName).build();
        return Response.status(Response.Status.NOT_FOUND).entity("User or Review not found").build();
    }

}
