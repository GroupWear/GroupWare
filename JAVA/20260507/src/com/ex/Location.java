package com.ex;

import java.util.*;

public class Location {
	private String city;
	private double longitude;// 경도
	private double latitude;// 위도
	
	
	public Location(String city, double longitude, double latitude) {
		this.city = city;
		this.longitude = longitude;
		this.latitude = latitude;
	}


	public void setCity(String city) {
		this.city = city;
	}


	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}


	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}


	public String getCity() {
		return city;
	}


	public double getLongitude() {
		return longitude;
	}


	public double getLatitude() {
		return latitude;
	}
	
	
	
	
}
