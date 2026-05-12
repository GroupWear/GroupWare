package com.ex;
import java.util.*;
public class Exam03 {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		HashMap<String, Integer> nations = new HashMap<>(); 
		
		System.out.println("나라 이름과 인구를 입력하세요");
		
		while(true) { // 입력
			System.out.print("나라 이름 인구 : ");
			String na = sc.next(); // 나라 이름
			if(na.equals("그만")) {
				while(true) {
					System.out.print("인구 검색 :");
					na = sc.next();
					if(nations.containsKey(na)) {
						System.out.println(na+"의 인구는"+nations.get(na));
					}
					if(!nations.containsKey(na)) {
						System.out.println("없는 나라입니다.");
					}
					if(na.equals("종료")) {
						System.out.println("종료합니다.");
						return;
					}
				}
			}
			int human = sc.nextInt(); // 인구수
			nations.put(na,human);
		}
	}
}
