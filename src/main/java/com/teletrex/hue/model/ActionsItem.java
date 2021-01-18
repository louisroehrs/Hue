package com.teletrex.hue.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ActionsItem{

	@JsonProperty("address")
	private String address;

	@JsonProperty("method")
	private String method;

	@JsonProperty("body")
	private Body body;
}