package com.redhat.quarkusfest.forecast.resources;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.redhat.quarkusfest.forecast.model.ErrorMessage;

@Provider
public class ExceptionToErrorMessageMapper implements ExceptionMapper<Exception> {

	@Override
	public Response toResponse(Exception exception) {

		int status = Status.INTERNAL_SERVER_ERROR.getStatusCode();

		if (exception instanceof WebApplicationException) {
			status = ((WebApplicationException) exception).getResponse().getStatus();
		}

		return Response.status(status).entity(new ErrorMessage(status, exception.getMessage())).build();

	}

}
