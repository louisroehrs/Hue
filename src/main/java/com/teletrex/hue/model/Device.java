package com.teletrex.hue.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Device{

	@JsonProperty("create date")
	private String createDate;

	@JsonProperty("last use date")
	private String lastUseDate;

	@JsonProperty("name")
	private String name;

	@JsonProperty("id")
	private String id;
}