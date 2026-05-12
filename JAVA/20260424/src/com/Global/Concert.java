package com.Global;
import java.util.*;
public class Concert {
	
	private String hallName;
	private Group[] group = new Group[3];
	private Scanner sc;
	
	public Concert(String hallName) {
		this.hallName = hallName;
		
		group[0] = new Group('S', 10);
		group[1] = new Group('A', 10);
		group[2] = new Group('B', 10);
		
		sc = new Scanner(System.in);
	}
	public void reserve(){
		System.out.print("좌석 구분 S(1), A(2), B(3) => ");
		int type = sc.nextInt();
		
		if(type < 1 || type > 3) {
			System.out.println("잘못된 좌석 타입입니다.");
			return;
		}
		//예약
		group[type-1].reserve();
	}
		
	
	public void search() {
		for(int i = 0; i < group.length; i++) {
			group[i].show();
		}
		System.out.println("조회를 완료하였습니다.");
	}
	public void cancel() {
		System.out.print("좌석 구분 S(1), A(2), B(3) => ");
		int type = sc.nextInt();
		
		if(type < 1 || type > 3) {
			System.out.println("잘못된 좌석 타입입니다.");
			return;
		}
		//예약
		group[type-1].cancel();
	}
	public void finish() {
		System.out.println(hallName+"예약 프로그램을 종료합니다.");
		return;
	}
	public void run() {
		System.out.println(hallName+"예약 프로그램입니다.");
		int menu = 0;
		while(menu != 4) {
			System.out.print("1.예약, 2.조회, 3.취소, 4.종료 : ");
			menu = sc.nextInt();
			
			switch(menu) {
			case 1:
					reserve();
				break;
				case 2:
					search();
				break;
				case 3:
					cancel();
				break;
				case 4:
					finish();
				break;
			default:
				break;
			}
		}
		
	}
	
}
