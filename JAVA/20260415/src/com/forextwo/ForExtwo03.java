package com.forextwo;

public class ForExtwo03 {

	public static void main(String[] args) {
		
		for(int i = 2; i<10;i++) {// 단
			for(int j = 1; j <10; j++) {// 안쪽 : 1 ~ 9 
				System.out.printf("%3d X %3d = %3d\t",i,j,i*j);				
			}
			System.out.println();
		}
		
	}

}
