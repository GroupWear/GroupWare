package com.interexam;

public class SCV  extends GroupUnit implements Repairable{

	public SCV() {
		super(60);
		hitPoint = MAX_HP;
	}
	void repair(Repairable r) {
		if(r instanceof Unit) {
			Unit u = (Unit)r;
			while (u.hitPoint != u.MAX_HP) {
				//Unit의 HP를 회복시켜 줌
				u.hitPoint++;
			}
			System.out.println(u.toString()+"수리가 완료되었습니다.");
		}
	}
	@Override
	public String toString() {
		return "SCV";
	}
	
}
