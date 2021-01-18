package com.teletrex.hue.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Action{

	@JsonProperty("xy")
	private List<Double> xy;

	@JsonProperty("ct")
	private int ct;

	@JsonProperty("alert")
	private String alert;

	@JsonProperty("sat")
	private int sat;

	@JsonProperty("effect")
	private String effect;

	@JsonProperty("bri")
	private int bri;

	@JsonProperty("hue")
	private int hue;

	@JsonProperty("colormode")
	private String colormode;

	@JsonProperty("on")
	private boolean on;
}