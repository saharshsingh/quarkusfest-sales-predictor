package com.redhat.quarkusfest.forecast.resources;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.redhat.quarkusfest.forecast.services.StoresService;

@Path("/api/stores")
public class StoresResource {

	private final StoresService service;

	@Inject
	public StoresResource(StoresService service) {
		this.service = service;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Integer> list() {
		return service.getStoreIDs();
	}

}