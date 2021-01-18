package com.teletrex.hue.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class Lights{

	@JsonProperty("light")
	private String lights;

	public String getLights() {
		return lights;
	}
}