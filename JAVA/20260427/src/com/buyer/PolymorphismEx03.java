package com.buyer;

public class PolymorphismEx03 {

	public static void main(String[] args) {
		
		Buyer b = new Buyer(800);
		TV tv = new TV();
		Computer com = new Computer();
		Audio au = new Audio();
		
		b.buy(tv);
		b.buy(com);
		b.buy(au);
		b.refund(tv);
		
		
		b.summary();
		
		
	}

}
