package org.bigBrotherBooks.auth;

import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.auth.principal.ParseException;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import org.bigBrotherBooks.constants.GlobalConstants;
import org.bigBrotherBooks.contexts.UserContext;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.io.IOException;

@Provider
public class JwtAuthenticationFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private final JWTParser jwtParser;
    private final UserContext.Provider userContextProvider;
    private static final String LOGIN_PATH = "/auth/login";
    private static final String REGISTER_PATH = "/auth/register";
    private static final String REFRESH_PATH = "/auth/refresh";

    @Inject
    public JwtAuthenticationFilter(JWTParser jwtParser, UserContext.Provider userContextProvider) {
        this.jwtParser = jwtParser;
        this.userContextProvider = userContextProvider;
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String uriPath = requestContext.getUriInfo().getPath();
        if (uriPath.contains(LOGIN_PATH) || uriPath.contains(REGISTER_PATH) || uriPath.contains(REFRESH_PATH)) {
            return;
        }

        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("Missing or Invalid authorization header").build());
            return;
        }
        String token = authorizationHeader.substring("Bearer".length()).trim();

        try {
            JsonWebToken jsonWebToken = jwtParser.parse(token);
            if (!jsonWebToken.getIssuer().equals(GlobalConstants.JWT_ISSUER)) {
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("Invalid token").build());
                return;
            }
            if (jsonWebToken.getExpirationTime() * 1000 < System.currentTimeMillis()) {
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("Token expired").build());
                return;
            }

            UserContext userContext = new UserContext();
            userContext.setUsername(jsonWebToken.getSubject());
            userContext.setRoles(jsonWebToken.getGroups().stream().toList());
            userContextProvider.setUserContext(userContext);

        } catch (ParseException e) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized").build());
        }
    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) throws IOException {
        userContextProvider.setUserContext(null);
    }
}
