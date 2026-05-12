package com.classex;

public class Day {

	private String work;// 하루에 할 일을 입력받는 문자열

	public String getWork() {
		return work;
	}

	public void setWork(String work) {
		this.work = work;
	}
	public void show() {
		if(work == null)
			System.out.println("없습니다.");
		else
			System.out.println(work+"입니다.");
	}
}
