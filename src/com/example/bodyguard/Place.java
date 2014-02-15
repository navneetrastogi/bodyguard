package com.example.bodyguard;

import java.io.Serializable;

import com.google.api.client.util.Key;

/** Implement this class from "Serializable"
* So that you can pass this class Object to another using Intents
* Otherwise you can't pass to another actitivy
* */
public class Place implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -114587526244839875L;

	@Key
	public String id;
	
	@Key
	public String name;
	
	@Key
	public String reference;
	
	@Key
	public String icon;
	
	@Key
	public String vicinity;
	
	@Key
	public Geometry geometry;
	
	@Key
	public String formatted_address;
	
	@Key
	public String formatted_phone_number;

	@Override
	public String toString() {
		return name + " - " + id + " - " + reference;
	}
	
	public static class Geometry implements Serializable
	{
	/**
	     * 
	     */
	private static final long serialVersionUID = 7729243354955696468L;
		@Key
		public Location location;
	}
	
	public static class Location implements Serializable
	{
	/**
	     * 
	     */
	private static final long serialVersionUID = -1124494405198483737L;

		@Key
		public double lat;
		
		@Key
		public double lng;
	}
	
}
