package com.forextwo;

public class example {

	public static void main(String[] args) {
		int s =0,e=0;
		for(int z = 1; z <=3 ; z++) {// 줄
			if(z==1) {
				s = 2;
				e = 4;
			}else
			if(z==2) {
				s = 5;
				e = 7;
			}else
			if(z==3) {
				s = 8;
				e = 9;
			}
				for(int i = 2; i<10;i++) {// 단
					for(int j = s; j <=e; j++) {// 안쪽 : 1 ~ 9 
							System.out.printf("%3d X %3d = %3d\t",j,i,i*j);
					}
					System.out.println();
				}
				System.out.println();
			}
		}
	}
