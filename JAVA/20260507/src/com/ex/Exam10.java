package com.ex;

import java.util.*;


public class Exam10 {

	private Vector<Nation> v = new Vector<>();
	private Scanner sc = new Scanner(System.in);
	
		public Exam10() {
		v.add(new Nation("독일","베를린" ));
		v.add(new Nation("중국","베이징" ));
		v.add(new Nation("러시아","모스크바" ));
		v.add(new Nation("그리스","아테네" ));
		v.add(new Nation("영국","런던" ));
		v.add(new Nation("일본","도쿄" ));
		v.add(new Nation("멕시코","멕시코시티" ));
		v.add(new Nation("스페인","리스본" ));
		v.add(new Nation("프랑스","파리" ));
	}
	public void error(String msg) {
		System.out.println(msg);
	}
	
	public void run() {
		System.out.println("수도 맞추기 게임을 시작합니다.");
		while(true) {
			System.out.print("입력 : 1, 퀴즈:2, 종료:3 >>");
			int menu = sc.nextInt();
			switch (menu) {
			case 1: input(); break;
			case 2: quiz(); break;
			case 3: finish();return;
			default:error("잘못된 입력입니다.");
				break;
			}
		}
	}
	public boolean contains(String country) {
		for(int i = 0; i < v.size();i++) {
			if(v.get(i).getCountry().equals(country)){
				// 사용자가 입력한 나라가 이미 있다면 
				return true;
			}
		}
		return false;
	}
	
	public void input() {
		int n = v.size();
		System.out.println("현재 "+n+"개 나라와 수도가 입력 되어있습니다.");
		n++;
		while(true) {

			System.out.println("나라와 수도 입력 "+n+" >>");
			String country = sc.next(); // 나라
			if(country.equals("그만")) {
				break;
			}
			String capital = sc.next(); // 수도
			
			if(contains(country)) {
				System.out.println(country+"는 이미 있습니다.");
				continue;
			}
			v.add(new Nation(country, capital));
			n++;
		}
	}
	
	public void quiz() {
		//모든 키(나라)를 알아내야함.
		while(true) {
			// 나라 중 하나를 선택 
			int index = (int)(Math.random()*v.size());
			
			// 문제와 정답을 결정함.
			Nation nation = v.get(index);
			String question = nation.getCountry();
			String answer = nation.getCapital();
			
			// 문제 출력
			System.out.println(question+"의 수도는 ? ");
			//사용자가 정답입력
			String capitalInput = sc.next();
			if(capitalInput.equals("그만")) { // quiz 종료
				break;
			}
			if(!capitalInput.equals(answer)) {
				System.out.println("정답!!");
				break;
			}else {
				System.out.println("아닙니다!!");
			}
		}
	}
	
	public void finish() {
		System.out.println("프로그램을 종료합니다.");
	}
	public static void main(String[] args) {
		new Exam10().run();
		
	}
}
