package com.interexam;

public class RepairableEx {

	public static void main(String[] args) {
		Tank t = new Tank();
		Dropship d = new Dropship();
		Marin m = new Marin();
		SCV s = new SCV();
		
		s.repair(t);
		s.repair(d);
//		s.repair(m);
		s.repair(s);
		

	}

}
