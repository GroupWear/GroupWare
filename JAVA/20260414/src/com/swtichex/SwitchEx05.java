package com.swtichex;

/*
 * 		문]
 * 			컴퓨터와 인간의 가위, 바위, 보 게임을 구현한다.
 * 			인간 가위(1), 바위(2), 보(3) 중 하나를 입력하고,
 * 			컴퓨터는 랜덤으로 임의의 수를 출력하여
 * 			비교한 후 승리를 결정한다.
 *
 * 			출력결과
 * 			가위(1), 바위(2), 보(3) : 1
 * 			인간 : 1
 * 			컴퓨터 : 1 
 * 			비겼습니다.
 * 			
 * 			출력결과
 * 			가위(1), 바위(2), 보(3) : 3
 * 			인간 : 3
 * 			컴퓨터 : 2 
 * 			인간이 이겼습니다.
 */

import java.util.*;
public class SwitchEx05 {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		
		
		System.out.print("가위(1), 바위(2), 보(3) : ");
		int human = sc.nextInt();
		int computer = (int)(Math.random() * 3) +1; // 1, 2, 3
		// Match.random() : 범위 - > 0.0 ~ 1.0 사이의 double형 실수값을 반환.
		// 1 ~ 3 사이의 정수값을 구하려면 * 3을 하여 구한다.
		// 0.0 * 3 <= Math.random() * 3 <= 1.0 * 3
		//		(int)(0.0+ 1 <= Math.random() * 3 + 1 <= 3.0 + 1)
		//		 1	<=	Math.random() * 3 < 4
		// 구한 값을 형변환한다.
		System.out.println("인간 : "+human);
		System.out.println("컴퓨터 : "+computer);
		
		switch (human-computer) {// human 에서 computer 값을 빼면 작은 정수로 변환하여
						// 0, 1, 2로 변환하여 처리함.
		case 0://무승부인경우
			System.out.println("비겼습니다.");
			break;
		case 1: case -2: //인간이 승리한경우
			System.out.println("인간이 이겼습니다.");
			break;
		case 2 : case -1:// 컴퓨터가 승리한경우
			System.out.println("컴퓨터가 이겼습니다.");
			break;
			
		default:System.out.println("값의 범위를 확인하시오.");
			break;
		}
		
	}

}
