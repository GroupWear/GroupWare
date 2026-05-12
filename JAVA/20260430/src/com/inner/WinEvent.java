package com.inner;

import java.awt.*;
import java.awt.event.*;
public class WinEvent extends WindowAdapter {

	@Override
	public void windowClosing(WindowEvent e) {
		System.exit(0);
	}
	
	
}
