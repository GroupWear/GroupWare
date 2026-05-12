package com.ex;
import java.util.*;
public class MyStack<T> implements IStack<T> {
	ArrayList<T> I = null;
	public MyStack() {
		I = new ArrayList<T>();
	}
	
	
	@Override
	public T pop() { // 스택에서 제거
		
		if(I.size() == 0)
			return null;
		else
				return I.remove(0);
	}

	@Override
	public boolean push(T ob) {// 스택에 저장
		I.add(0, ob);
		
		return false;
	}

}
