package com.classex;

public class Add {
	private int a;
	private int b;
	
	public void setValue(int a, int b) {
		this.a = a;
		this.b = b;
	}
	public int calculate() {
	 return a+b;	
	}
}


/*
	int a, b;
 	char op;
 
	System.out.print(" 두 정수와 연산자 입력 : ");
 	a = sc.nextInt();
	b = sc.nextInt();
	c = sc.next().charAt(0);
 
 	switch(op) {
 		case '+':
			Add add = new Add();
					add.setValue(n1,n2);
					System.out.println(add.calculate());
  			break;
 * 
 * 
 */


