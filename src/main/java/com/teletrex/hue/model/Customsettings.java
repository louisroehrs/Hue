package com.teletrex.hue.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Customsettings{

	@JsonProperty("xy")
	private List<Double> xy;

	@JsonProperty("bri")
	private int bri;
}