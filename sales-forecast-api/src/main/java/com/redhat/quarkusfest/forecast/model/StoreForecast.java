package com.redhat.quarkusfest.forecast.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StoreForecast {

	private final String date;
	private final int storeID;
	private final int itemID;
	private final int units;

	@JsonCreator
	public StoreForecast(@JsonProperty("date") String date, @JsonProperty("storeID") int storeID,
			@JsonProperty("itemID") int itemID, @JsonProperty("units") int units) {
		this.date = date;
		this.storeID = storeID;
		this.itemID = itemID;
		this.units = units;
	}

	@JsonProperty("date")
	public String getDate() {
		return date;
	}

	@JsonProperty("storeID")
	public int getStoreID() {
		return storeID;
	}

	@JsonProperty("itemID")
	public int getItemID() {
		return itemID;
	}

	@JsonProperty("units")
	public int getUnits() {
		return units;
	}

	@Override
	public String toString() {
		return "StoreForecast [date=" + date + ", storeID=" + storeID + ", itemID=" + itemID + ", units=" + units + "]";
	}

}
