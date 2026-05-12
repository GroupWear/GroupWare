package com.arraytwoex;

/*		문]
 * 			한 학생에 대한 성적 처리 프로그램을 구현하시오.
 * 			이름, 과목 점수를 입력받고,
 * 			총점과 평균, 학점을 출력한다.
 * 			1차원 배열을 활용 
 * 
 * 
 */
import java.io.*;

public class ArraytwoEx09 {
	
	public static void main(String[] args) throws IOException {
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		System.out.print("이름 입력 : ");
		String name = br.readLine();
		
		String[] subject = {"국어","영어","수학" };
		//String[] subject = new String[args.length];
		
//		for(int i =0; i < args.length;i++) {
//			subject[i] = args[i];
//		System.out.println(subject[i]);
//		}
//		
		
//		for(int i =0; i < subject.length ;i++) {
//			subject[i] = args[i];
//			System.out.println(subject[i]);
//		}
		
//		for(int i =0; i < subject.length ;i++) {
//			System.out.println(subject[i]);
//		}
		
		int[] jumsu = new int[subject.length+1]; // 4 -> 국어, 영어, 수학, 총점을 저장할 공간 확보.
		
		for(int i =0; i < jumsu.length-1; i++) {
			System.out.print(subject[i]+"점수 : ");
			//과목별 점수를 입력받는다.
			jumsu[i] = Integer.parseInt(br.readLine());
			//입력 받은 점수의 합계
			jumsu[jumsu.length-1] += jumsu[i];
			
		}
		System.out.printf("총점 : %d%n",jumsu[jumsu.length-1]);
		
		float avg = (float)jumsu[jumsu.length-1]/subject.length;
		//평균을 구하고 소수점 3자리에서 반올림처리 함
		avg = (int)((avg+0.005) *100)/100.f; // 그냥 공식임 
		System.out.println("평균 : "+avg);
		
		// 평균을 이용하여 학점을 구한다.
		char grade = ' ';
		switch ((int)(avg/10)) {
		case 10:
		case 9:
			grade = 'A';
			break;
		case 8:
			grade = 'B';
			break;
		case 7:
			grade = 'C';
			break;
		case 6:
			grade = 'D';
			break;
		default:grade = 'F';
			break;
		}
		System.out.println("--------------------");
		System.out.print("이름\t");
		for(int i = 0; i <subject.length; i++) {
			System.out.print(subject[i]+"\t");
		}
		System.out.print("총점\t평균\t학점");
		System.out.println();
		System.out.print(name+"\t");
		for(int i =0; i < subject.length; i++) {
			System.out.print(" "+jumsu[i]+"\t");
		}
		System.out.println(jumsu[jumsu.length-1]+" "+avg+"\t"+" "+grade);
		System.out.println("--------------------");
	}

}
