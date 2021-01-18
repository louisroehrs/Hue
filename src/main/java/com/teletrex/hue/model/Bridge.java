package com.teletrex.hue.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Bridge{

	@JsonProperty("lastinstall")
	private String lastinstall;

	@JsonProperty("state")
	private String state;
}