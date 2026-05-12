package com.forextwo;

/*		문]
 * 			
 * 
 * 
 */
public class ForExtwo01 {//중첩 for문을 의

	public static void main(String[] args) {
//		System.out.println("**********");
//		
//		System.out.println("**********");
//		
//		System.out.println("**********");
//		
//		System.out.println("**********");
//		
//		System.out.println("**********");
		
		for(int i = 0; i < 5; i++) {// 가로(행)
			for(int j = 0; j <= i; j++) {// 세로(열)
				System.out.print("*");
			}
			System.out.println();
		}

}
}
