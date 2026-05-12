package com.example;
import java.util.*;
public class StackMain {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		System.out.print("총 스택 저장 공간의 크기 입력 :  ");
		int n = sc.nextInt();
		
		StackApp s =new StackApp(n); 
		
		//데이터를 입력
		while(true) {
			System.out.print("문자열 입력 : ");
			String str = sc.next();
			if(str.equals("그만")) {
				System.out.println("프로그램을 종료합니다.");
				System.exit(0);
			}
			boolean r = s.push(str);
			if(r == false) {
				System.out.println("스택이 꽉 찼습니다.");
				break;
			}
			
		}
		// 결과 출력
		System.out.print("스택에 저장된 모든 문자열 팝 : ");
		int len = s.length();
		for(int i = 0; i < len; i++) {
			System.out.print(s.pop()+", ");
		}
	}
}




