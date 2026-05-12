package com.exceptionex;

public class ExceptionEx05 {

	int[] ss;
	
	public ExceptionEx05()	{
		ss = new int[3];
		
	}
	public void program(){
		
		for(int i =0; i <= ss.length;i++) {
			System.out.println("for문의 시작 "+i+"번째");
			try {
				System.out.println(ss[i]);
			} catch (Exception e) {
				//System.out.println("Exception 발생"+e);
				//	System.out.println(e.getMessage());
				e.printStackTrace();
				
				/*		Exception 클래스의 메시지 출력 메소드
				 * 	
				 * 		getMessage() : 
				 * 		 - 발생한 예외 클래스 인스턴스에 저장된 메시지를 얻어서 출력함
				 * 		printStackTrace() :
				 * 		 - 예외 발생 당시의 호출스택에 있었던 메소드의 정보와 예외 메시지를 화면에 출력
				 * 
				 */
				return;
			}finally {
				System.out.println("무조건 출력");
			}
			
			System.out.println("for문의 끝 "+i+"번째");
		}
	}
	
	public static void main(String[] args) {
		
		ExceptionEx05 e = new ExceptionEx05();
		e.program();
		
	}

}
