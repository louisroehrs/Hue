package com.teletrex.hue.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class State{

	@JsonProperty("all_on")
	private boolean allOn;

	@JsonProperty("any_on")
	private boolean anyOn;
}