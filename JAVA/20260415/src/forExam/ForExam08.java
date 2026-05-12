package forExam;

/*		문]
 * 			임의의 정수를 입력받아 입력받은 정수의 3승수를 출력하는 프로그램을 작성하시오.
 * 
 * 			승수 입력 :5 
 * 			3^5 = 243
 * 	
 */
import java.util.*;
public class ForExam08 {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		
		System.out.print("승수 입력 : ");
		int n = sc.nextInt();
		int i,com = 1;
		
		for(i=1; i <= n; i++) {
			com *= 3;
		}
		System.out.printf("3 ^ %d = %d", n,com);
	}
	}
