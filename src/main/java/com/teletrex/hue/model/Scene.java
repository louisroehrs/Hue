package com.teletrex.hue.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Scene{

	@JsonProperty("owner")
	private String owner;

	@JsonProperty("image")
	private String image;

	@JsonProperty("type")
	private String type;

	@JsonProperty("version")
	private int version;

	@JsonProperty("picture")
	private String picture;

	@JsonProperty("name")
	private String name;

	@JsonProperty("recycle")
	private boolean recycle;

	@JsonProperty("lastupdated")
	private String lastupdated;

	@JsonProperty("id")
	private String id;

	@JsonProperty("locked")
	private boolean locked;

	@JsonProperty("lights")
	private List<String> lights;

	@JsonProperty("group")
	private String group;

	@JsonProperty("appdata")
	private Appdata appdata;
}