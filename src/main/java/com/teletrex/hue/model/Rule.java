package com.teletrex.hue.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Rule{

	@JsonProperty("owner")
	private String owner;

	@JsonProperty("created")
	private String created;

	@JsonProperty("name")
	private String name;

	@JsonProperty("recycle")
	private boolean recycle;

	@JsonProperty("id")
	private String id;

	@JsonProperty("timestriggered")
	private int timestriggered;

	@JsonProperty("conditions")
	private List<ConditionsItem> conditions;

	@JsonProperty("actions")
	private List<ActionsItem> actions;

	@JsonProperty("lasttriggered")
	private String lasttriggered;

	@JsonProperty("status")
	private String status;
}