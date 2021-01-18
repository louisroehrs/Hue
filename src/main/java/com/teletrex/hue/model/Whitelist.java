package com.teletrex.hue.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Whitelist{

	@JsonProperty("device")
	private Device device;
}