package com.teletrex.hue.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Swupdate{

	@JsonProperty("lastinstall")
	private String lastinstall;

	@JsonProperty("state")
	private String state;

	@JsonProperty("devicetypes")
	private Devicetypes devicetypes;

	@JsonProperty("updatestate")
	private int updatestate;

	@JsonProperty("text")
	private String text;

	@JsonProperty("checkforupdate")
	private boolean checkforupdate;

	@JsonProperty("url")
	private String url;

	@JsonProperty("notify")
	private boolean notify;
}