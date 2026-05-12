package com.ex;

import java.util.*;

public class Exam06 {

	private Scanner sc = new Scanner(System.in);
	private HashMap<String, Location> map = new HashMap<>();
	
	public void input() {
		System.out.println("도시이름, 경도, 위도를 입력하세요.");
		
		for(int i =0; i < 4;i++) {
			System.out.print(">>");
			String city = sc.next();
			double longitude = sc.nextDouble();
			double latitude = sc.nextDouble();
			
			Location loc = new Location(city, longitude, latitude);
			map.put(city, loc);
		}
	}
	public void print() {
		Set<String> key = map.keySet();
		Iterator<String> it = key.iterator();
		System.out.println("-------------------------");
		while(it.hasNext()) {
			String city = it.next();
			Location loc = map.get(city);
			
			System.out.print(loc.getCity());
			System.out.print(loc.getLongitude());
			System.out.println(loc.getLatitude());
//			System.out.print(loc.getCity()+", "+loc.getLongitude()+", "+loc.getLatitude());
		}

		
	}
	public void search() {
		while(true) {
			System.out.print("도시 이름 : ");
			String city = sc.next();
			if(city.equals("그만")){
				System.out.println("프로그램을 종료합니다.");
				return;
			}
			Location loc = map.get(city);
			if(loc == null) {
				System.out.println(city+"는 없습니다.");
			}else {
				System.out.print(loc.getCity());
				System.out.print(loc.getLongitude());
				System.out.println(loc.getLatitude());
			}
		}
	}
	
	public void run() {
		input();
		print();
		search();
	}
	
	public static void main(String[] args) {
		Exam06 a = new Exam06();
		a.run();
		
		
	}
}
