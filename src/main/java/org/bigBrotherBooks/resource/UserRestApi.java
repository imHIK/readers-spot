package org.bigBrotherBooks.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.bigBrotherBooks.model.User;
import org.bigBrotherBooks.service.UserUtils;

@Path("/user")
public class UserRestApi {

    UserUtils userUtils;

    @Inject
    public UserRestApi(UserUtils userUtils) {
        this.userUtils = userUtils;
    }

    @GET
    @Path("/{user_name}")
    @Produces("application/json")
    public Response getUser(@PathParam("user_name") String userName) {
        User user = userUtils.getUserById(userName);
if(user == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("User " + userName + " not found").build();
        }
        return Response.ok(user).build();
    }

    @DELETE
    @Path("/{user_name}")
    @Produces("application/json")
    public Response deleteUser(@PathParam("user_name") String userName) {
        User user = userUtils.getUserById(userName);
        if(user == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("User " + userName + " not found").build();
        }
        userUtils.deleteUser(userName);
        return Response.ok("User " + userName + " deleted successfully").build();
    }

    @POST
    @Path("/follow/{user_name}")
    @Produces("application/json")
    public Response followUser(@PathParam("user_name") String toUserName, @QueryParam("from") String fromUserName) {
        User toUser = userUtils.getUserById(toUserName);
        User fromUser = userUtils.getUserById(fromUserName);
        if(toUser == null || fromUser == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
        }
        fromUser.followUser(toUserName);
        userUtils.saveUser(fromUser);
        return Response.ok("User " + fromUserName + " is now following " + toUserName).build();
    }


}
