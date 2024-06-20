package org.bigBrotherBooks.resource;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
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
    @Produces("application/json")
    public Response getUser(@PathParam("user_name") String userName) {
        User user = userService.getUserById(userName);
if(user == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("User " + userName + " not found").build();
        }
        return Response.ok(user).build();
    }

    @DELETE      // TODO: hard and soft delete
    @Path("/{user_name}")
    @Produces("application/json")
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
    @Produces("application/json")
    public Response followUser(@PathParam("user_name") String toUserName, @QueryParam("from") String fromUserName) {
        User toUser = userService.getUserById(toUserName);
        User fromUser = userService.getUserById(fromUserName);
        if(toUser == null || fromUser == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
        }
        fromUser.followUser(toUserName);
        userService.saveUser(fromUser);
        return Response.ok("User " + fromUserName + " is now following " + toUserName).build();
    }

    @POST
    @Path("/unfollow/{user_name}")
    @Produces("application/json")
    public Response unfollowUser(@PathParam("user_name") String toUserName, @QueryParam("from") String fromUserName) {
        User toUser = userService.getUserById(toUserName);
        User fromUser = userService.getUserById(fromUserName);
        if(toUser == null || fromUser == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
        }
        fromUser.unfollowUser(toUserName);
        userService.saveUser(fromUser);
        return Response.ok("User " + fromUserName + " has unfollowed " + toUserName).build();
    }

    @POST
    @Path("/updateProfile")
    @Produces("application/json")
    public Response updateProfile(@Valid UserProfileUpdateDTO userProfileUpdateDTO) {
        userService.updateProfile(userProfileUpdateDTO);
        return Response.ok("Profile updated successfully").build();
    }

}
