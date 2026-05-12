package com.inner;

import java.awt.*;
import java.awt.event.*;
public class WindwosEventEx02 extends Frame implements WindowListener{

	public WindwosEventEx02() {
		super("Event02");
		addWindowListener(this);
		setSize(400, 300);
		setVisible(true);
	}
	
	public static void main(String[] args) {
		
		new WindwosEventEx02();
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
	
		
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		// TODO Auto-generated method stub
		System.exit(0);
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
