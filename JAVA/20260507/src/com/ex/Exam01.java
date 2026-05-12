package com.ex;
import java.util.*;
public class Exam01 {
	
	static void print(Vector<Integer> v) {
		int max = v.get(0);
		
	for(int i = 0; i < v.size(); i++) {
		if(max < v.get(i)) {
			max = v.get(i);
		}
		
	}
	System.out.print("가장 큰 수 : "+max);
}

	public static void main(String[] args) {
		
		Vector<Integer> v = new Vector<Integer>();
		
		Scanner sc = new Scanner(System.in);
		
		
		
		while(true) {
			System.out.print("정수 : ");
			int n = sc.nextInt();
			if(n == -1) {
				System.out.println("종료");
				break;
			}
			v.add(n);
			
			
		}
		if(v.size() == 0) {
			System.out.println("벡터가 비어있음");
			return;
		}
		print(v);
		}

		
		
		
	}

