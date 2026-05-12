package com.example;
public class StackApp implements Stack {
	//스택에 저장할 공간
	private String[] element;
	// 인덱스
	private int tos;
	
	public StackApp(int capacity) {
		element = new String[capacity];
		tos = -1; // 위와같이 계산했을때 마지막데이터를 출력함. 그래서 -1을 해줌 가장 마지막 데이터
	}
	
	@Override
	public int length() {
		return tos + 1;
	}

	@Override
	public int capacity() {
		return element.length;
	}

	@Override
	public String pop() {
		// 스택이 비어 있으면 null을 리턴
		if(tos == -1) {
			return null;
		}
		// 스택이 비어 있지 않으면 맨위에 있는 값을 가져오고 
		else {
			String s = element[tos]; // 맨 위에있는 데이터
		// 스택에 위치가 감소가 됨.
		tos--;
		return s;
		}
	}

	@Override
	public boolean push(String val) {
		// 스택이 다 차있으면 
		if(tos == element.length-1) {
			return false;
		}
		else {
			// 스택이 안 차있으면 -> 입력받은 값을 저장
			tos++;
			// 스택의 위치가 증가됨
			element[tos] = val;
			return true;
		}

	}
}
