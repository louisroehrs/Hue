package com.teletrex.hue.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Resource{

	@JsonProperty("owner")
	private String owner;

	@JsonProperty("classid")
	private int classid;

	@JsonProperty("name")
	private String name;

	@JsonProperty("recycle")
	private boolean recycle;

	@JsonProperty("description")
	private String description;

	@JsonProperty("links")
	private List<String> links;

	@JsonProperty("id")
	private String id;

	@JsonProperty("type")
	private String type;
}