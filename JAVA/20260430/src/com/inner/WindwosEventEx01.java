package com.inner;

import java.awt.*;


public class WindwosEventEx01 {

	public static void main(String[] args) {
		
		Frame f = new Frame("Evnet01");
		WinEvent we = new WinEvent();
		f.addWindowListener(we);
		f.setSize(400,300);
		f.setVisible(true);
	}
	
}
