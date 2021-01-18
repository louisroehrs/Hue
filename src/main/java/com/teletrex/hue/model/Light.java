package com.teletrex.hue.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Setter;

@Setter
public class Light{

	@JsonProperty("swupdate")
	private Swupdate swupdate;

	@JsonProperty("capabilities")
	private Capabilities capabilities;

	@JsonProperty("productid")
	private String productid;

	@JsonProperty("modelid")
	private String modelid;

	@JsonProperty("state")
	private StateLight state;

	@JsonProperty("swversion")
	private String swversion;

	@JsonProperty("type")
	private String type;

	@JsonProperty("manufacturername")
	private String manufacturername;

	@JsonProperty("name")
	private String name;

	@JsonProperty("swconfigid")
	private String swconfigid;

	@JsonProperty("productname")
	private String productname;

	@JsonProperty("id")
	private String id;

	@JsonProperty("config")
	private Config config;

	@JsonProperty("uniqueid")
	private String uniqueid;
}