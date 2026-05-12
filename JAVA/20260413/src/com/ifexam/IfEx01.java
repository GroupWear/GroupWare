package com.ifexam;
/*		
 * i
 *  문]
 *  90점이상a
 *  80점이상b
 *  70점이상c
 *  60점이상d
 *  50점이하 f
 */

import java.util.*;

public class IfEx01 {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		System.out.print("점수를 입력하세요. : ");
		int a = sc.nextInt();
		char grade = ' ';
		
		
		if (a >= 90) {
			grade ='A';
		}else if(a >= 80 ) {
			grade ='B';
		}else if(a >= 70) {
			grade ='C';
		}else if(a >= 60) {
			grade ='D';
		}else {
			grade ='F';
		}
		System.out.println("당신의 학점은 "+grade+"입니다.");
		
		sc.close();
	}

}
