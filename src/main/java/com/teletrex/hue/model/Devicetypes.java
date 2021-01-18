package com.teletrex.hue.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Devicetypes{

	@JsonProperty("sensors")
	private List<Object> sensors;

	@JsonProperty("bridge")
	private boolean bridge;

	@JsonProperty("lights")
	private List<Object> lights;
}