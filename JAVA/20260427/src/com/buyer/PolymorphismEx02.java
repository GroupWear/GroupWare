package com.buyer;

public class PolymorphismEx02 {

	public static void main(String[] args) {
		
		Buyer b = new Buyer(800);
		
		b.buy(new TV());
		b.buy(new Computer());
		b.buy(new Audio());
		b.summary();
		
		
		
		
	}

}
