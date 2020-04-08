package com.redhat.quarkusfest.forecast.resources;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import com.redhat.quarkusfest.forecast.model.StoreForecast;

@Path("/api/forecasts")
public class ForecastsResource {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<StoreForecast> getForecast(@QueryParam("date") String date, @QueryParam("storeID") Integer storeID) {

		// validate args
		final List<String> errors = new ArrayList<>();
		final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		try {
			LocalDate.parse(date, formatter);
		} catch (final Exception e) {
			errors.add("'date' must be provided in format 'yyyy-MM-dd");
		}

		if (storeID == null) {
			errors.add("'storeID' required");
		}

		if (errors.size() > 0) {
			throw new WebApplicationException(errors.toString(), Status.BAD_REQUEST);
		}

		return List.of(new StoreForecast("2020-04-08", 1, 1, 5), new StoreForecast("2020-04-08", 1, 2, 5),
				new StoreForecast("2020-04-08", 1, 3, 5), new StoreForecast("2020-04-08", 1, 4, 5),
				new StoreForecast("2020-04-08", 1, 5, 5));
	}

}
