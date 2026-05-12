package com.classex;

/*		문]
 * 				MonthSchedule클래스에 Day 객체배열과 적절한 필드, 메소드를 작성하고,
 * 				실행결과와 같이 3가지 기능을 가진 프로그램을 작성하시오.
 * 	
 * 				MonthSchedule클래스에는 생성자, input(), view(), finish(), run()
 * 				실행 결과
 * 				 
 * 				이번달 스케줄 관리 프로그램 
 * 				할일(입력 : 1, 보기 : 2, 종료 : 3) : 1
 * 				날짜(1~30) : 1
 * 				할일(빈칸없이 입력) : 자바공부
 * 				
 *				할일(입력 : 1, 보기 : 2, 종료 : 3) : 2
 * 				날짜(1~30) : 1
 * 				1일의 할일은 자바공부입니다.
 * 
 * 				할일(입력 : 1, 보기 : 2, 종료 : 3) : 3
 * 				날짜(1~30) : 1
 * 				종료 합니다.
 */
import java.util.*;
public class MonthSchedule {
	//필드 선언

	
	//날짜 필드
	private int nDays;// 한달의 날짜
	//Day 객체 배열 선언
	private Day[] days;
	// 입력은 스캐너
	private Scanner sc;
	
	// 생성자에서 날짜, 배열에 크기, 스캐너 객체 생성
	public MonthSchedule(int nDays) {
		this.nDays = nDays;
		// 객체 배열을 공간할당
		this.days = new Day[nDays];
		for(int i = 0; i < days.length; i++) {
			days[i] = new Day();
		}
		sc = new Scanner(System.in);
	}
	public void input() {
		System.out.println("날짜(1~30) : ");
		int day = sc.nextInt();
		System.out.println("할일(빈칸없이 입력) : ");
		String work = sc.next();
		day--;
		if(day < 0 || day > nDays) {// 0~29까지의 유효범위 있는지 
			System.out.println("날짜를 잘못 입력 하셨습니다. 다시 입력 해주세요.");
			return;
		}
		days[day].setWork(work);
	}
	public void view() {
		System.out.println("날짜(1~30) : ");
		int day = sc.nextInt();
		day--;
		if(day < 0 || day > nDays) {// 0~29까지의 유효범위 있는지 
			System.out.println("날짜를 잘못 입력 하셨습니다. 다시 입력 해주세요.");
			return;
		}
		System.out.println(day+1+"일 할일 ");
		days[day].show();
	}
	public void finish() {
		System.out.println("종료 합니다.");
		System.exit(0);
	}
	public void run() {
		System.out.println("이번달 스케줄 관리 프로그램");
		while(true) {
			System.out.print("할일(입력 : 1, 보기 : 2, 종료 : 3) : ");
			int menu = sc.nextInt();
			
			switch (menu) {
			case 1:
				input();
				break;
			case 2:
				view();
				break;
			case 3:
				finish();
				break;
			default:System.out.println("다시 입력해주세요.");
				break;
			}
		}
	}
	
	public static void main(String[] args) {
		MonthSchedule apri = new MonthSchedule(30);
		apri.run();
		
		
	}

}
