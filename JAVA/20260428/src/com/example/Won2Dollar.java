package com.example;

public class Won2Dollar extends Converter {
	
	private double dollar;
	
	public Won2Dollar(double dollar) {
		this.dollar = dollar;
	}
	@Override
	protected double convert(double src) {
		
		return src/dollar;
	}
	@Override
	protected String getSrcString() {
		return "원";
	}
	@Override
	protected String getDestString() {
		return "$";
	}
	
	public static void main(String[] arg) {
		Won2Dollar wd = new Won2Dollar(1500); //1달러에 1500원
		wd.run();
	}
}	

