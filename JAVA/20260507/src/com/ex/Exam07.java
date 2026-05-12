package com.ex;
import java.util.*;

public class Exam07 {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		HashMap<String, Double> s = new HashMap<>();
		double n = 0;
		String name = "0";
		System.out.println("글로벌장학금관리시스템.");
		for(int i = 0; i < 5; i++) {
			System.out.print("이름과 학점 :");
			name = sc.next();
			n = sc.nextDouble();
			
			s.put(name, n);
			
		}
		double num = 0;
		System.out.println("장학생 선발 기준 학점 : ");
		num = sc.nextDouble();
		System.out.print("장학생 : ");
        for(String key : s.keySet()) {
            if(s.get(key) >= num) {
                System.out.print(key + "  ");
            }
			
		}
	/*
		Set<String> key = map.keySet();
		Iterator<String> it = key.iterator();
		System.out.println("-------------------------");
		while(it.hasNext()) {
			String name = it.next();
			double n = map.get(name);
			if(score > n) {
			System.out.print(name+" ");
			}
			
	 */
		
		
	}
}
