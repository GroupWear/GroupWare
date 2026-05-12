package com.ex;
import java.util.*;
public class Exam08 {

	private Scanner sc = new Scanner(System.in);
	private HashMap<String, Integer> map = new HashMap<>();
	
	public Exam08() {	}
	public void input() {
		System.out.println("포인트 관리 프로그램입니다.");
		while(true) {
			System.out.print("이름과 포인트 입력 : ");
			String name = sc.next();
			if(name.equals("그만")) {
				System.out.println("종료합니다.");
				return;
			}
			int point = sc.nextInt();
			Integer n = map.get(name);
			if(n != null) {
				point += n;
			}
			map.put(name, point);
			print();
		}
		
	}
	public void print() {
		Set<String> key = map.keySet();
		Iterator<String> it = key.iterator();
		while(it.hasNext()) {
			String name = it.next();
			int point = map.get(name);
	        System.out.print("("+name+", "+point+")");
		}
		System.out.println();
	}
	public void run() {
		input();
		
	}
	
	public static void main(String[] args) {
		
		new Exam08().run();
		
	}
}
