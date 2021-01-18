package com.teletrex.hue.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Appdata{

	@JsonProperty("data")
	private String data;

	@JsonProperty("version")
	private int version;
}