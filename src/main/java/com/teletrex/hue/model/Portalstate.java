package com.teletrex.hue.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Portalstate{

	@JsonProperty("incoming")
	private boolean incoming;

	@JsonProperty("outgoing")
	private boolean outgoing;

	@JsonProperty("communication")
	private String communication;

	@JsonProperty("signedon")
	private boolean signedon;
}