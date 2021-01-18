package com.teletrex.hue.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Internetservices{

	@JsonProperty("swupdate")
	private String swupdate;

	@JsonProperty("remoteaccess")
	private String remoteaccess;

	@JsonProperty("time")
	private String time;

	@JsonProperty("internet")
	private String internet;
}