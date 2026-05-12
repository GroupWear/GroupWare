package com.ex;
import java.awt.print.Printable;
import java.util.*;
public class Exam02 {

	
	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in); 
		
		ArrayList<Character> arr = new ArrayList<Character>();
		
		for(int i = 0; i< 6; i++) {
			System.out.print("학점 입력 : ");
			char ch = sc.next().charAt(0);
			if((ch >= 'A' && ch <= 'D') || ch == 'F') {
				arr.add(ch);
			}else {
				System.out.println("학점이 아닙니다.");
				break;
			}
		}
		System.out.println(arr);
		double hak = 0.0;
		for(int i = 0; i< 6; i++) {
			char ch = arr.get(i);
			switch(ch) {
			case 'A' : hak += 4.0;break;
			case 'B' : hak += 3.0;break;
			case 'C' : hak += 2.0;break;
			case 'D' : hak += 1.0;break;
			case 'F' : hak += 0.0;break;
			}
		}
		double avg = hak/arr.size();
		System.out.println("평균 : "+avg);
	}


}

