package com.teletrex.hue.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Swupdate2{

	@JsonProperty("autoinstall")
	private Autoinstall autoinstall;

	@JsonProperty("bridge")
	private Bridge bridge;

	@JsonProperty("state")
	private String state;

	@JsonProperty("checkforupdate")
	private boolean checkforupdate;

	@JsonProperty("lastchange")
	private String lastchange;
}