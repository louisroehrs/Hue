package com.teletrex.hue.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Group{

	@JsonProperty("sensors")
	private List<Object> sensors;

	@JsonProperty("name")
	private String name;

	@JsonProperty("recycle")
	private boolean recycle;

	@JsonProperty("action")
	private Action action;

	@JsonProperty("id")
	private String id;

	@JsonProperty("state")
	private State state;

	@JsonProperty("type")
	private String type;

	@JsonProperty("class")
	private String jsonMemberClass;

	@JsonProperty("lights")
	private List<String> lights;
}