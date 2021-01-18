package com.teletrex.hue.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Autoinstall{

	@JsonProperty("updatetime")
	private String updatetime;

	@JsonProperty("on")
	private boolean on;
}