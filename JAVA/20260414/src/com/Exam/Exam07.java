package com.Exam;

/*		문]
 * 			2차원 평면에서 직사각형은 왼쪽 상단 모서리와 오른쪽 하단 모서리의
 * 			두 점으로 표현한다.
 * 			x(100, 100)과 y(200, 200)의 두 점으로 이루어진 사각형이 있을때
 * 			
 * 			정수 x와 정수 y값을 입력받고 점(x, y)가 직사각형 안에 있는지를 판별하는 프로그램을 구현하시오.
 * 			
 * 			출력 결과
 * 			점(x, y)의 좌표를 입력 : 150 150
 * 			(150, 150)는 사각형 안에 있습니다.
 * 
 * 
 */

import java.util.*;
public class Exam07 {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		
		System.out.print("점(x, y)의 좌표를 입력 : ");
		int x = sc.nextInt();
		int y = sc.nextInt();
		
		if((x >= 100 && y >= 100)&&(x <= 200&& y >= 200)){
			System.out.printf("(%d, %d)는 사각형 안에 있습니다.",x,y);
		}else {
			System.out.printf("(%d, %d)는 사각형 안에 없습니다.",x,y);
		}
		
	}

}
