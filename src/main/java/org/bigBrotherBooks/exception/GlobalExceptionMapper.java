package org.bigBrotherBooks.exception;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.bigBrotherBooks.logger.LogType;
import org.bigBrotherBooks.logger.Logger;
import org.bigBrotherBooks.logger.LoggerFactory;

/**
 * Catch-all mapper that converts uncaught exceptions into a consistent JSON
 * {@link ApiError} response instead of leaking stack traces to the client.
 * More specific mappers (and JAX-RS built-ins) take precedence over this one.
 */
@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Exception> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionMapper.class);

    @Override
    public Response toResponse(Exception exception) {
        if (exception instanceof WebApplicationException webEx) {
            Response original = webEx.getResponse();
            int status = original.getStatus();
            String reason = Response.Status.fromStatusCode(status) != null
                    ? Response.Status.fromStatusCode(status).getReasonPhrase()
                    : "Error";
            String message = original.hasEntity() ? String.valueOf(original.getEntity()) : webEx.getMessage();
            return Response.status(status)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(new ApiError(status, reason, message))
                    .build();
        }

        LOGGER.log(LogType.ERROR, "Unhandled exception", exception);
        int status = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
        return Response.status(status)
                .type(MediaType.APPLICATION_JSON)
                .entity(new ApiError(status, "Internal Server Error",
                        "An unexpected error occurred. Please try again later."))
                .build();
    }
}
