package com.teletrex.hue.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Schedule{

	@JsonProperty("localtime")
	private String localtime;

	@JsonProperty("created")
	private String created;

	@JsonProperty("name")
	private String name;

	@JsonProperty("recycle")
	private boolean recycle;

	@JsonProperty("description")
	private String description;

	@JsonProperty("id")
	private String id;

	@JsonProperty("time")
	private String time;

	@JsonProperty("command")
	private Command command;

	@JsonProperty("status")
	private String status;
}