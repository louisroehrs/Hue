package com.teletrex.hue.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Sensor{

	@JsonProperty("state_sensor")
	private StateSensor stateSensor;

	@JsonProperty("modelid")
	private String modelid;

	@JsonProperty("manufacturername")
	private String manufacturername;

	@JsonProperty("name")
	private String name;

	@JsonProperty("swversion")
	private String swversion;

	@JsonProperty("id")
	private String id;

	@JsonProperty("type")
	private String type;

	@JsonProperty("config")
	private Config config;
}