package com.vlivin.moneytransfer.exceptions;

import org.glassfish.grizzly.http.util.HttpStatus;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class IllegalStateExceptionMapper implements ExceptionMapper<IllegalStateException> {
    @Override
    public Response toResponse(IllegalStateException e) {
        return Response.status(HttpStatus.BAD_REQUEST_400.getStatusCode()).
                entity(e.getMessage()).
                type("text/plain").
                build();
    }
}
