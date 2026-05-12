package com.example;
/*
 * 문]
 * 		이름, 주소, 나이, 체중, 독신여부를 입력받고 출력하는 프로그램을 구현하시오.
 * 		입력은 Scanner 클래스사용.
 * 
 * 		String -> 도시, 이름
 * 		int -> 나이
 * 		double -> 체중
 * 		boolean -> 독신여부 
 */
import java.util.*;
public class Exam02 {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		
		String str1, str2;
		int age;
		double d1;
		boolean b1;
		
		System.out.println("도시 이름 : ");
		str1 = sc.nextLine();
		System.out.println("이름 :");
		str2 = sc.nextLine();
		
		System.out.println("나이 :");
		age = sc.nextInt();
		
		System.out.println("체중 :");
		d1 = sc.nextDouble();
		
		System.out.println("독신여부 :");
		b1 = sc.nextBoolean();
		
		sc.close();
	}

}
//package com.example;
//import java.util.*;
//public class Exam03 {
//	public static void main(String[] args) {
//		
//		Scanner sc = new Scanner(System.in);
//		
//		String name = sc.next();
//		System.out.print("이름은 "+name+",");
//		
//		String city = sc.next();
//		System.out.print("도시는"+city+",");
//		
//		int age = sc.nextInt();
//		System.out.print("나이는"+age+"살,");
//		
//		double weight = sc.nextDouble();
//		System.out.print("체중은"+weight+"kg,");
//		
//		boolean single = sc.nextBoolean();
//		System.out.print("독신 여부는"+single+"입니다.");
//		
//		sc.close();
//		
//	}
//}

