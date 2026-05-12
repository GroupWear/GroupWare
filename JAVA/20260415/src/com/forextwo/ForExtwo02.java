package com.forextwo;

public class ForExtwo02 {

	public static void main(String[] args) {
		
		for(char i = 'A'; i <='Z';i++) {
			//System.out.print(i);
			for(int j = 0; j <i-65;++j) {
				System.out.print(" ");
			}
			for(char c =i; c<='Z'-(i-65);++c) {
				System.out.print("*");
			}
			System.out.println();
		}
		
	}

}
