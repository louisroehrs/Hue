package com.teletrex.hue.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Ct{

	@JsonProperty("min")
	private int min;

	@JsonProperty("max")
	private int max;
}