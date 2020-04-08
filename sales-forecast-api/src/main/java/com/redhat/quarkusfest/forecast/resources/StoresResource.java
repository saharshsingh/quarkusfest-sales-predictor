package com.redhat.quarkusfest.forecast.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/api/stores")
public class StoresResource {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Integer> list() {
		return List.of(1, 2, 3, 4, 5, 6, 6, 8, 9, 10);
	}

}