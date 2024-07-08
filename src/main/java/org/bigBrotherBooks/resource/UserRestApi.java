package org.bigBrotherBooks.resource;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bigBrotherBooks.dto.ReviewDTO;
import org.bigBrotherBooks.dto.UserDTO;
import org.bigBrotherBooks.dto.UserProfileUpdateDTO;
import org.bigBrotherBooks.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Path("/user")
public class UserRestApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserRestApi.class);
    private final UserService userService;

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
    public Response saveUser(@Valid UserDTO userDTO) {
        LOGGER.info("Save User");
        userService.saveUser(userDTO);
        return Response.status(Response.Status.CREATED).entity("User " + userDTO.getUserName() + " saved successfully").build();
    }

    @POST
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(@Valid UserDTO userDTO) {
        LOGGER.info("Update User");
        if (userService.updateUser(userDTO)) {
            return Response.ok("User " + userDTO.getUserName() + " updated successfully").build();
        }
        return Response.status(Response.Status.NOT_FOUND).entity("User " + userDTO.getUserName() + " not found").build();
    }

    @DELETE      // TODO: hard and soft delete
    @Path("/{user_name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUser(@PathParam("user_name") String userName) {
        LOGGER.info("Delete User");
        if (userService.deleteUser(userName)) {
            return Response.ok("User " + userName + " deleted successfully").build();
        }
        return Response.status(Response.Status.NOT_FOUND).entity("User " + userName + " not found").build();
    }

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers(@QueryParam("user_names") List<String> userNames) {
        List<UserDTO> users = userService.getUserDTOs(userNames);
        return Response.ok(users).build();
    }

    @GET
    @RolesAllowed("ADMIN")
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
    @Path("/followers/{user_name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFollowers(@PathParam("user_name") String userName) {
        List<UserDTO> followers = userService.getFollowers(userName);
        return Response.ok(followers).build();
    }

    @POST
    @Path("/following/{user_name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFollowing(@PathParam("user_name") String userName) {
        List<UserDTO> following = userService.getFollowing(userName);
        return Response.ok(following).build();
    }

    @POST
    @Path("/updateProfile/{user_name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateProfile(@PathParam("user_name") String userName, @Valid UserProfileUpdateDTO userProfileUpdateDTO) {
        LOGGER.info("Update Profile");
        if (userService.updateProfile(userName, userProfileUpdateDTO)) {
            return Response.ok("Profile updated successfully").build();
        }
        return Response.status(Response.Status.NOT_FOUND).entity("User " + userName + " not found").build();
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

    @GET
    @Path("/reviews/{user_name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReviews(@PathParam("user_name") String userName) {
        List<ReviewDTO> reviews = userService.getReviews(userName);
        return Response.ok(reviews).build();
    }

}
