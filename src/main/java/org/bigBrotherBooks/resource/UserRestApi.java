package org.bigBrotherBooks.resource;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bigBrotherBooks.dto.UserDTO;
import org.bigBrotherBooks.dto.UserProfileUpdateDTO;
import org.bigBrotherBooks.model.User;
import org.bigBrotherBooks.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Path("/user")
public class UserRestApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserRestApi.class);
    UserService userService;

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
    @Path("/users")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers(@Valid List<String> userNames) {
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
        User toUser = userService.getUserById(toUserName);
        User fromUser = userService.getUserById(fromUserName);
        if(toUser == null || fromUser == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
        }
        fromUser.followUser(toUser);
        userService.saveUser(fromUser);
        return Response.ok("User " + fromUserName + " is now following " + toUserName).build();
    }

    @POST
    @Path("/unfollow/{user_name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response unfollowUser(@PathParam("user_name") String toUserName, @QueryParam("from") String fromUserName) {
        LOGGER.info("Unfollow User");
        User toUser = userService.getUserById(toUserName);
        User fromUser = userService.getUserById(fromUserName);
        if(toUser == null || fromUser == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
        }
        fromUser.unfollowUser(toUser);
        userService.saveUser(fromUser);
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

}
