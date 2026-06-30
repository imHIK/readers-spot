package org.bigBrotherBooks.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.HashMap;
import java.util.Map;

/**
 * Turns bean-validation failures ({@code @Valid}, {@code @NotBlank}, ...) into a
 * 400 response that lists the offending fields.
 */
@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        Map<String, String> fieldErrors = new HashMap<>();
        for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
            fieldErrors.put(violation.getPropertyPath().toString(), violation.getMessage());
        }
        int status = Response.Status.BAD_REQUEST.getStatusCode();
        ApiError error = new ApiError(status, "Bad Request", "Validation failed");
        if (!fieldErrors.isEmpty()) {
            error.setFieldErrors(fieldErrors);
        }
        return Response.status(status)
                .type(MediaType.APPLICATION_JSON)
                .entity(error)
                .build();
    }
}
