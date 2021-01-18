package com.teletrex.hue.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Streaming{

	@JsonProperty("proxy")
	private boolean proxy;

	@JsonProperty("renderer")
	private boolean renderer;
}