package com.rect;

import java.awt.Color;

public class ColorMain {

	public static void main(String[] args) {
		
		Point2 p = new Point2();
		p.set(1,2);
		p.showPoint();
		
		ColorPoint cp = new ColorPoint();
		cp.set(3,4);
		cp.setColor("red");
		cp.colorshowPoint();
		
		
		
	}

}
