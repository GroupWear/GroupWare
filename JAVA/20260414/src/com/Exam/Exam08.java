package com.Exam;

/*		문]
 * 			2차원 평면에서 직사각형 x, y의 두 좌표를 표현한다.
 * 			직사각형을 구성하는 두점(x1, y1), (x2, y2)를 입력받아
 * 			(100, 100), (200, 200)의 두 점으로 이루어진 직사각형과 
 * 			충돌하는지를 판별하는 프로그램을 작성하시오
 * 			

 * 			사각형 안에 있는지를 판별하는 메소드
 * 			public static boolean inRect(int x, int y, int rectx1, int recty1, int rectx2, int rexty2)		
 * 			
 * 			if((x >= rectx1 && x <= rectx2)&&(y >- recty1 && y <= recxty2)){
 * 					return true;
 * 			}else{
 * 			return false;
 * 			}
 *	 		점 (x, y)가 (rectx1, recty1), (rectx2, recty2)의 사각형 안에 있으면			
 *			true 값을 반환하고, 없으면 false 값을 리턴하는 메소드
 * 
 */
import java.util.*;

public class Exam08 {
	
	public static void main(String[] args) {
		
		int x1, y1, x2, y2;
		Scanner sc = new Scanner(System.in);
		System.out.print("두 점(x1, y1),(x2, y2)의 좌표 입력 : ");
		x1 = sc.nextInt();
		y1 = sc.nextInt();
		x2 = sc.nextInt();
		y2 = sc.nextInt();
		
	 	// x1, y1 : x2, y2 : x1, y2 : x2, y1
		
		if(inRect(x1, y1, 100, 100, 200, 200) ||
				inRect(x2, y2, 100, 100, 200, 200)||
				inRect(x1, y2, 100, 100, 200, 200)||
				inRect(x2, y1, 100, 100, 200, 200)) {
				System.out.println("사각형이 겹칩니다.");
		}else if(inRect(x1, y1, 100, 100, 200, 200) &&
				inRect(x2, y2, 100, 100, 200, 200)&&
				inRect(x1, y2, 100, 100, 200, 200)&&
				inRect(x2, y1, 100, 100, 200, 200)) {
				System.out.println("사각형이 겹칩니다.");
		}else if(inRect(100, 100, x1, y1, x2, y2)&&
				inRect(100, 200, x1, y1, x2, y2)&&
				inRect(200, 100, x1, y1, x2, y2)&&
				inRect(200, 200, x1, y1, x2, y2)) {
			System.out.println("사각형이 겹칩니다.");
		}else {
			System.out.println("겹치지않습니다.");
		}
		
	}
	
	public static boolean inRect(int x, int y, int rectx1, int recty1, int rectx2, int recty2) {
	    if((x >= rectx1 && x <= rectx2) && (y >= recty1 && y <= recty2)) {
	        return true;
	    } else {
	        return false;
	    }
	}
}

