package com.teletrex.hue.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StateSensor{

	@JsonProperty("daylight")
	private boolean daylight;

	@JsonProperty("lastupdated")
	private String lastupdated;
}