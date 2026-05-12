package com.ifexam;

/*		문]
 * 			사용자로부터 임의의 알파벳 한 문자를 입력받아.
 * 			입력받은 알파벳이 모음인지를 판정하는 프로그램을 구현하시오.
 * 			단, 대소문자를 모두 적용하고, 알파벳 이외의 문자가 입력되면, 
 * 			입력 오류 처리 하시오.
 */
import java.util.*;
public class Ifex07 {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		System.out.print("문자를 입력 : ");
		char ch = sc.next().charAt(0);
		
		if(('a' <= ch && ch <= 'z')|| ('A' <= ch && 'Z' >= ch)) {
			
			if((ch == 'a' || ch == 'e'|| ch == 'i' || ch == 'o' || ch == 'u')||
					(ch == 'A' || ch == 'E'|| ch == 'I' || ch == 'O' || ch == 'U')) {
				
				System.out.println("모음입니다.");
				
			}else System.out.println("자음입니다.");
			
		}else System.out.println("알파벳이 아닙니다.");
		
		sc.close();
		
		
		
	}
}
