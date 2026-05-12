package com.point;

public class Point3D extends Point {
	
	private int z;
	public Point3D(int x, int y, int z) {
		super(0,0);
		this.z=z;
		
	}
	@Override
	public String toString() {
//		String str =("("+getX()+", "+getY()+", "+z+")의 점");
//		return str;
		return "("+getX()+", "+getY()+", "+z+")의 점";
	}
	public void moveUP() {
		z++;
	}
	public void moveDown() {
		z--;
	}

	public void move(int x, int y, int z) {
		super.move(x,y);
		this.z=z;
	}
	
	public static void main(String[] args) {
		Point3D p = new Point3D(1, 2, 3);
		System.out.println(p.toString()+"입니다.");
		
		p.moveUP();
		System.out.println(p.toString()+"입니다.");
		
		p.moveDown();
		p.move(10,10);
		System.out.println(p.toString()+"입니다.");
		
		
		p.move(100,200,300);
		System.out.println(p.toString()+"입니다.");
		
		
	}

}
