package com.classex;

/*		문] 여러개의 클래스를 활용하여 프로그램을 구현하시오.
 * 			더하기, 빼기, 곱하기, 나누기를 수행하는 각 클래스의 이름을 만드시오 
 * 			클래스 --> Add, Sub, Mul, Div
 * 
 * 			각 클래스마다 구성되는 필드와 메소드는 동일하다.
 * 				int 타입(자료형)의 a, b 필드 -> 피 연산자(첫번째 수 , 두번째 수) 
 *				void setValue(int a, int b) 피연산의 값을 객체 저장한다.
 *				int calculate() : 클래스 내에서 연산을 수행하고 결과를 리턴해주는 메소드 				
 * 				
 * 				main() 메소드를 가지고 있는 실행 클래스인 Calc 클래스에서 
 * 				두 정수와 연산자를 입력받아 Add, Sub, Mul, Div 중에서 
 * 				이 연산을 실행할 수 있는 객체를 생성하고, 
 * 				메소드(setValue, calculate)를 호출하여 결과를 출력하는 프로그램을 구현한다.
 * 
 * 				출력 결과
 * 				두 정수와 연산자 입력 : 5 7 *
 * 				35
 */

import java.util.*;

public class Calc {
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		
		while(true){
			System.out.print(" 두 정수와 연산자 입력 : ");
			int n1 = sc.nextInt();
			int n2 = sc.nextInt();
			String c = sc.next();
			
			if (c.equals("+")) {
				Add add = new Add();
				add.setValue(n1,n2);
				System.out.println(" "+add.calculate());

			}
			if (c.equals("-")) {
				Sub sub = new Sub();
				sub.setValue(n1,n2);
				System.out.println(" "+sub.calculate());

			}
			if (c.equals("*")) {
				Mul mul = new Mul();
				mul.setValue(n1,n2);
				System.out.println(" "+mul.calculate());

			}
			if (c.equals("/")) {
				Div div = new Div();
				div.setValue(n1,n2);
				System.out.println(" "+div.calculate());
				
			if(c.equals("그만")) {
				System.out.println("종료합니다");
				return;
			}
			else {
				System.out.println("잘못된 입력입니다.");
			//return;
				
			}
		}
	}
	}
	}

