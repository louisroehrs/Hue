package com.teletrex.hue.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Backup{

	@JsonProperty("errorcode")
	private int errorcode;

	@JsonProperty("status")
	private String status;
}