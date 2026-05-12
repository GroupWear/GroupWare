package com.tv;

public class ColorTV extends TV {
	
	
	private int color;
	public void setColor(int color) {
		this.color = color;
	}

	public ColorTV(int size, int color) {
		super(size);
		this.color = color;
		
	}
	
	public int getColor() {
		return color;
	}

	public void PrintProperty() {
		System.out.println(" 주소의 "+setSize()+"인치"+color+"컬러");
	}
	
	public static void main(String[] args) {
		ColorTV myTV = new ColorTV (32,1024);
		myTV.PrintProperty();
		
		
	}

}
