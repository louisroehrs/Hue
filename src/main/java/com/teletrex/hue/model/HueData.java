package com.teletrex.hue.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HueData{

	@JsonProperty("sensors")
	private Sensors sensors;

	@JsonProperty("schedules")
	private Schedules schedules;

	@JsonProperty("scenes")
	private Scenes scenes;

	@JsonProperty("groups")
	private Groups groups;

	@JsonProperty("rules")
	private Rules rules;

	@JsonProperty("config")
	private Config config;

	@JsonProperty("lights")
	private Lights lights;

	@JsonProperty("resourcelinks")
	private Resourcelinks resourcelinks;
}