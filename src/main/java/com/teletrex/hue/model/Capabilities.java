package com.teletrex.hue.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Capabilities{

	@JsonProperty("streaming")
	private Streaming streaming;

	@JsonProperty("certified")
	private boolean certified;

	@JsonProperty("control")
	private Control control;
}