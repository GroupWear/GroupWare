package com.ifexam;

/*	문]
 * 		세과목을 점수를 입력받아총점과 평균을 구하고
 * 		평균을 이용하여 학점을 판정하는 프로그램을 구현하시오.
 * 		단 총점을 정수, 평균은 소수점이하 2자리까지 출력하시오.
 */
import java.util.*;
public class Ifex06 {

	public static void main(String[] args) {
		Scanner sc =  new Scanner(System.in);
		
		int kor, eng, mat, sum=0;
		double avg=0;
		char grade=' ';
		
		System.out.print("국어 점수 : ");
		kor = sc.nextInt();
		System.out.print("영어 점수 : ");
		eng = sc.nextInt();
		System.out.print("수학 점수 : ");
		mat = sc.nextInt();
		
		sum = kor+eng+mat;
		
		avg = (double)sum/3;
		
		System.out.println("총 점수 : "+sum);
		
		
		if (avg >= 90) {
			grade ='A';
		}else if(avg >= 80 ) {
			grade ='B';
		}else if(avg >= 70) {
			grade ='C';
		}else if(avg >= 60) {
			grade ='D';
		}else {
			grade ='F';
		}
		System.out.printf("평균 점수 : %.2f점 \n학점은 %s입니다.",avg,grade);
		
		sc.close();
	}
}
