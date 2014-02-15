package com.example.bodyguard;

/**
 * 
 */


import org.codehaus.jackson.annotate.JsonProperty;


/**
 * @author abhineet
 *
 */
public class UserInformation 
{

	public class Coordinates
	{
		private Double latitude;
		private Double longitude;
		
		public Coordinates(Double latitude, Double longitude)
		{
			this.latitude = latitude;
			this.longitude = longitude;
		}
		
		@JsonProperty(value = "latitude")
		public Double getLatitude() {
			return latitude;
		}
		
		public void setLatitude(Double latitude) {
			this.latitude = latitude;
		}
		
		@JsonProperty(value = "longitude")
		public Double getLongitude() {
			return longitude;
		}
		public void setLongitude(Double longitude) {
			this.longitude = longitude;
		}
		
		public Double getDistance()
		{
			return this.latitude + this.longitude;
		}
		
	}
	
    public static String deviceId;
	private Coordinates cord;
	private String name;
    private long	phonenumber;
	

	@JsonProperty(value = "name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	

	@JsonProperty(value = "deviceId")
	public String getDeviceId() {
		return deviceId;
	}

    public static void setDeviceId(String dvcId)
	{
	    deviceId = dvcId;
	}
	
	@JsonProperty(value = "cord")
	public Coordinates getCord() {
		return cord;
	}
	
	public void setCord(Coordinates cord) {
		this.cord = cord;
	}

    @JsonProperty(value = "phonenumber")
    public long getPhonenumber()
	{
	    return phonenumber;
	}

    public void setPhonenumber(long phonenumber)
	{
	    this.phonenumber = phonenumber;
	}
}
