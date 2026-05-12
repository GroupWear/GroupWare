package com.arraytwoex;

/*		문]
 * 			5명의 학생의 세과목을 더해서 각 학생의 총점과 평균을 구하시오.
 * 			또한 각 과목별 총점을 계산하여 출력하시오.
 * 			2차원 배열을 활용하여 점수를 초기화시킨다음 진행 하도록한다.
 * 			
 * 			100, 100, 100
 * 			20, 20, 20
 * 			30, 30, 30
 * 			40, 40, 40
 * 			50, 50, 50
 * 
 */
public class ArraytwoEx04 {

	public static void main(String[] args) {
		
		int[][] arr =  { 
				{100, 100, 100},
				{20, 20, 20},	
				{30, 30, 30},
				{40, 40, 40},
				{50, 50, 50},
		};
		int kor = 0, eng = 0, mat =0;
		float avg= 0.0f;
		
		for(int i = 0; i < arr.length;i++) {
			int sum = 0;
			kor += arr[i][0];
			eng += arr[i][1];
			mat += arr[i][2];
			System.out.printf("%d.\t",i+1);
			
			for(int j = 0; j <arr[i].length;j++) {
				sum += arr[i][j];
				System.out.printf("%3d\t",arr[i][j]);
				
			}
			avg = sum/(float) arr[i].length;
			System.out.printf("총점 : %5d 평균 : %5.1f%n",sum, avg);
		}
		System.out.printf("합계 : %3d %4d %4d%n", kor,eng,mat);
	}

}
