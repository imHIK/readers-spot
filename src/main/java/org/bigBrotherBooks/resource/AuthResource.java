package org.bigBrotherBooks.resource;

import com.mysql.cj.util.StringUtils;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bigBrotherBooks.model.LoginRequest;
import org.bigBrotherBooks.service.AuthService;
import org.bigBrotherBooks.service.UserService;

@Path("/auth")
public class AuthResource {

    AuthService authService;
    UserService userService;

    @Inject
    public AuthResource(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @POST
    @Path("/login")         // how to implement so that the token check wont happen at login ?
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@Valid LoginRequest loginRequest) {
        String token = authService.authenticate(loginRequest);
        if (!StringUtils.isNullOrEmpty(token)) {
            return Response.ok().header(HttpHeaders.AUTHORIZATION, "Bearer " + token).build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @POST
    @Path("/register")
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(@Valid LoginRequest loginRequest) {
        // to be added in future
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @POST
    @Path("/refresh")
    @Produces(MediaType.APPLICATION_JSON)
    public Response refresh(@Valid LoginRequest loginRequest) {
        // to be added in future
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }
}
