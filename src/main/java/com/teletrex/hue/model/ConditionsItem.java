package com.teletrex.hue.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConditionsItem{

	@JsonProperty("address")
	private String address;

	@JsonProperty("value")
	private String value;

	@JsonProperty("operator")
	private String operator;
}