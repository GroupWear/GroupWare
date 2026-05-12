package com.Ex;

public class OperEx01 {
	public static void main(String[] args) {
		
		
		int i = 5, j = 0;
		j= i++;// j에 i의 대입을 먼저하고, j=5가 되니, 그 다음i++(5++)의 연산을 하니 6이 된 후 대입. i=6, j=6
		System.out.println("j=i++; 실행 후 i ="+i+"j="+j);
		i=5;
		j= 0;
		j= ++i;// 
		System.out.println("j=++i; 실행 후 i ="+i+"j="+j);
		
		i=5;
		j=5;
		i++;
		++j;
		System.out.println("i="+i+",j="+j);
	}
}
