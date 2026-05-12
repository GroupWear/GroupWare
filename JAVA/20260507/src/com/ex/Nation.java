package com.ex;

public class Nation {

	private String country;
	private String capital;
	
	public Nation(String country, String capital) {
		this.capital = capital;
		this.country = country;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCapital() {
		return capital;
	}

	public void setCapital(String capital) {
		this.capital = capital;
	}
}
