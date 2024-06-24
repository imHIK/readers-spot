package org.bigBrotherBooks.resource;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bigBrotherBooks.dto.UserProfileUpdateDTO;
import org.bigBrotherBooks.model.User;
import org.bigBrotherBooks.service.UserService;

@Path("/user")
public class UserRestApi {

    UserService userService;

    @Inject
    public UserRestApi(UserService userService) {
        this.userService = userService;
    }

    @GET
    @Path("/{user_name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("user_name") String userName) {
        User user = userService.getUserById(userName);
if(user == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("User " + userName + " not found").build();
        }
        return Response.ok(user).build();
    }

    @POST
    @Path("/save")
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveUser(@Valid User user) {
        userService.saveUser(user);
        return Response.status(Response.Status.CREATED).entity("User " + user.getUserName() + " saved successfully").build();
    }

    @DELETE      // TODO: hard and soft delete
    @Path("/{user_name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUser(@PathParam("user_name") String userName) {
        User user = userService.getUserById(userName);
        if(user == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("User " + userName + " not found").build();
        }
        userService.deleteUser(userName);
        return Response.ok("User " + userName + " deleted successfully").build();
    }

    @POST
    @Path("/follow/{user_name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response followUser(@PathParam("user_name") String toUserName, @QueryParam("from") String fromUserName) {
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
        userService.updateProfile(userProfileUpdateDTO);
        return Response.ok("Profile updated successfully").build();
    }

}
