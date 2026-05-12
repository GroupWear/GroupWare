package com.point;

public class ColorPoint extends Point {
	
	String color;
	
	public ColorPoint() {
		this(0, 0, "BLACK");
	}
	public ColorPoint(int x, int y, String color) {
		super(x,y);
		this.color= color;
	}
    public void setXY(int x, int y) {
    	move(x,y);
    }
    public void setColor(String color) {
		this.color = color;
	}
    @Override
    public String toString() {
    	String str = color+"색의 ("+getX()+", "+getY()+")의 점";
    	return str;
    }


	public static void main(String[] args) {
        ColorPoint cp = new ColorPoint(5, 5, "YELLOW");
        cp.setXY(10, 20);
        cp.setColor("RED");
        String str = cp.toString();
        System.out.println(str+"입니다");
	
	
	
	}
}

