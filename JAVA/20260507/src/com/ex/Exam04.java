package com.ex;
import java.util.*;
public class Exam04 {

	static void print(Vector<Integer> v) {
		int avg = 0;
		int sum = 0;
		for(int i = 0; i < v.size();i++) {
			sum += v.get(i);
			avg = sum/v.size();
		}
		System.out.print(avg);
	}
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		Vector<Integer> v = new Vector<>();
		
		int n = 0;
		
		while(true) {
			System.out.print("강수량 입력 : ");
			n = sc.nextInt();
			v.add(n);
			Iterator<Integer> it = v.iterator();
			while(it.hasNext()) {
				n = it.next();
				System.out.print(n+" ");
			}
			System.out.println();
			if(n == 0) {
				System.out.print("현재 강수량 평균 : ");
				print(v);
				return;
			}
		}
		
	}
}
