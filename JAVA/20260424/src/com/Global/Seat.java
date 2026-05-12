package com.Global;

public class Seat {

	private String name;
	
	public String getName() {
		return name;
	}
	
	public Seat() {
		name = null; //처음 예약하면 아무도없으니까 null로 초기화
		
	}

	public void cancel() {
		name = null;
	}
	public void reserve(String name) {
		this.name = name;
	}
	public boolean isOccupied() {//예약 여부를 판정하는 메소드
		if(name == null) {//좌석이 예약되어 있으면 true, 없으면 false
			return false;
		}else {// 이름이 있는 경우
			return true;
		}
			
	}
	// 이름이 존재하는지를 판정
	public boolean match(String name) {
		//이 클래스에 저장된 name이 매개변수로 전달받은 name과 같으면 리턴.
		return name.equals(this.name);
		
	}
}
