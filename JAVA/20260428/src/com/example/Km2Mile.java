package com.example;

public class Km2Mile extends Converter {

	private double mile;
	
	public Km2Mile(double mile) {
		this.mile = mile;
	}
	
	@Override
	protected double convert(double src) {
		
		return src/mile;
		
	}
	@Override
	protected String getSrcString() {
		return "Km";
	}

	@Override
	protected String getDestString() {
		return "mile";
	}
	
	public static void main(String[] args) {
		Km2Mile wd = new Km2Mile(1.6); //1달러에 1500원
		wd.run();
	}
}
