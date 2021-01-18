package com.teletrex.hue.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Control{

	@JsonProperty("ct")
	private Ct ct;

	@JsonProperty("mindimlevel")
	private int mindimlevel;

	@JsonProperty("maxlumen")
	private int maxlumen;

	@JsonProperty("colorgamut")
	private List<List<Double>> colorgamut;

	@JsonProperty("colorgamuttype")
	private String colorgamuttype;
}