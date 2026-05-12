package com.classexam;

/*		문]
 * 			1번은 두개의 정수를 입력받아 그 중 큰수를 출력하는 기능 
 * 			
 * 			2번은 두개의 정수를 입력받아 그 사이에 두 정수 사이의 합계를 구하는 기능 
 * 		
 * 			3번은 세개의 정수를 입력받아 큰 순서대로 출력하는 기능
 * 
 * 
 */
import java.util.*;
public class Exam04 {
	static Scanner sc = new Scanner(System.in);
	
	public static int aaa() {
		int a, b;
		System.out.print("첫번째 정수 : ");
		a = sc.nextInt();
		System.out.print("두번째 정수 : ");
		b = sc.nextInt();
		
		if(a> b ) {
			return a;
		}else {
			return b;
		}
			

	}
	public static void bbb() {
		int a, b, sum = 0;
		System.out.print("첫번째 정수 : ");
		a = sc.nextInt();
		System.out.print("두번째 정수 : ");
		b = sc.nextInt();
		
		if(a > b) {// 첫번째 정수가 두번째 정수보다 큰 경우 자리를 바꾼다.
			a = a^b;
			b = b^a;
			a = a^b;
		}
		for(int i = a; i <= b; i++) {
			sum += i;
		}
		System.out.println(a+" ~ "+b+"사이의 합계 : "+sum);
	}
	
	public static String ccc(int x, int y, int z) {
		
		if(y >= x && y >= z) {
			int imsi = x;
			x = y;
			y = imsi;
		}else if(z >= x && z >= y) {
			int imsi = x; 
			x = z;
			z = imsi;
		}
		if( z >= y) {
			int imsi = y;
			y = z;
			z = imsi;
		}
		String abc = x +" > "+ y + " > "+z;
		return abc;
	}
	
	public static void main(String[] args) {
		
		int x = 0; 
		
		while(true) {
			System.out.print("1. 최대값 2. 사이의 합계 3. 큰수대로 나열 4. 종료 : ");
			x = sc.nextInt();
			
			if(x == 1) {//리턴값을 가지고 있는 메소드 호출
				int k = aaa();
				System.out.println("두 정수중 최대값은 "+k+"입니다.");
			}else if(x == 2) {
				bbb();
			}else if(x == 3) {
				System.out.print("첫번째 정수 : ");
				int aa = sc.nextInt();
				System.out.print("두번째 정수 : ");
				int bb = sc.nextInt();
				System.out.print("세번째 정수 : ");
				int cc = sc.nextInt();
				String str = ccc(aa,bb,cc);
				System.out.println("큰 순서대로 나열 : "+str);
			}else if(x == 4) {
				System.out.println("프로그램을 종료합니다.");
				return; 
			}else {
				System.out.println("잘못 입력하셨습니다.");
			}
			System.out.println();
			
		}
		
	}
	
}
