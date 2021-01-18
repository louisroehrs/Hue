package com.teletrex.hue.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Config{

	@JsonProperty("archetype")
	private String archetype;

	@JsonProperty("startup")
	private Startup startup;

	@JsonProperty("function")
	private String function;

	@JsonProperty("direction")
	private String direction;

	@JsonProperty("localtime")
	private String localtime;

	@JsonProperty("ipaddress")
	private String ipaddress;

	@JsonProperty("swupdate")
	private Swupdate swupdate;

	@JsonProperty("internetservices")
	private Internetservices internetservices;

	@JsonProperty("proxyport")
	private int proxyport;

	@JsonProperty("backup")
	private Backup backup;

	@JsonProperty("UTC")
	private String uTC;

	@JsonProperty("timezone")
	private String timezone;

	@JsonProperty("proxyaddress")
	private String proxyaddress;

	@JsonProperty("linkbutton")
	private boolean linkbutton;

	@JsonProperty("replacesbridgeid")
	private Object replacesbridgeid;

	@JsonProperty("zigbeechannel")
	private int zigbeechannel;

	@JsonProperty("mac")
	private String mac;

	@JsonProperty("datastoreversion")
	private String datastoreversion;

	@JsonProperty("netmask")
	private String netmask;

	@JsonProperty("swupdate2")
	private Swupdate2 swupdate2;

	@JsonProperty("factorynew")
	private boolean factorynew;

	@JsonProperty("dhcp")
	private boolean dhcp;

	@JsonProperty("portalstate")
	private Portalstate portalstate;

	@JsonProperty("modelid")
	private String modelid;

	@JsonProperty("swversion")
	private String swversion;

	@JsonProperty("apiversion")
	private String apiversion;

	@JsonProperty("whitelist")
	private Whitelist whitelist;

	@JsonProperty("portalconnection")
	private String portalconnection;

	@JsonProperty("bridgeid")
	private String bridgeid;

	@JsonProperty("starterkitid")
	private String starterkitid;

	@JsonProperty("portalservices")
	private boolean portalservices;

	@JsonProperty("name")
	private String name;

	@JsonProperty("gateway")
	private String gateway;

	@JsonProperty("configured")
	private boolean configured;

	@JsonProperty("sunriseoffset")
	private int sunriseoffset;

	@JsonProperty("sunsetoffset")
	private int sunsetoffset;

	@JsonProperty("on")
	private boolean on;
}