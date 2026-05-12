package com.classex;

/*		Varargs(Variable Arguments)
 * 			- 메소드 정의시 통일된 인자의 자료형에 '...'라고 명시하므로 이를 통해 
 * 			   메소드 호출시 필요한 인자의 수를 유연하게 구현할 수 있다.
 * 			  (내부적으로 배열화 작업을 자동으로 해주기 때문임)
 */

public class VariableEx {
		
		public void argTest(String ...n) {
			for(int i = 0; i < n.length; i++) {
				System.out.print("n["+i+"] : "+n[i]+"\t\n");
				
			}
			System.out.println("--------------------");
		}
		
	public static void main(String[] args) {
		VariableEx ve = new VariableEx();
		
		ve.argTest("100","100");
		ve.argTest("100","100","100");
		ve.argTest("100","100","100", "100");
		ve.argTest("100","100","100", "100","100");
		
		
		
	}

}
