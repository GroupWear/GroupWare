package com.classex;

/*		문]
 * 			Grade 클래스를 완성하시오
 * 			세 과목의 점수를 입력받아 Grade 객체를 생성하고 
 * 			성적과 평균을 출력하는 프로그램을 완성하시오.
 * 			main() 메소드는 다음과 같이 활용
 */

import java.util.*;

public class Grade {
		// int 자료형의 국어, 영어, 수학
		private int kor;
		private int eng;
		private int mat;
		public int getKor() {
			return kor;
		}
		public int getEng() {
			return eng;
		}
		public int getMat() {
			return mat;
		}
		// private로 선언하고, 생성자와 세과목의 평균을 리턴하는 average() 메소드를 작성하시오
		private Grade() {
		}
		private Grade(int a, int b, int c) {
			this.kor = a;
			this.eng = b;
			this.mat = c;
		}
		public int average() {
			return (kor+eng+mat)/3;
		}
		
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		System.out.print("국어 영어 수학 순으로 점수 입력 : ");
		int kor = sc.nextInt();
		int eng = sc.nextInt();
		int mat = sc.nextInt();
		
		Grade me = new Grade(kor,eng,mat);
		System.out.println("평균은"+me.average()+"점 입니다.");
	}

}
