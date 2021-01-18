package com.teletrex.hue.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Startup{

	@JsonProperty("mode")
	private String mode;

	@JsonProperty("configured")
	private boolean configured;

	@JsonProperty("customsettings")
	private Customsettings customsettings;
}