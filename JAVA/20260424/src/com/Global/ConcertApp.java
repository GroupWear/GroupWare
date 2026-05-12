package com.Global;
import java.util.*;
public class ConcertApp {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
//		System.out.println("예약:1, 조회:2, 취소:3, 종료:4 : ");
//		int n = sc.nextInt();
		Concert s = new Concert("글로벌인 예약 프로그램");
		s.run();
		
	}
}
