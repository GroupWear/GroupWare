package com.tv;

public class IPTV extends ColorTV {
	private String ip;
	public IPTV(String ip,int size, int color) {
		super(size,color);
		this.ip = ip;
	}
	public void PrintProperty() {
		System.out.print("나의 IPTV는 "+ip);
		super.PrintProperty();
	}

	public static void main(String[] args) {
		IPTV iptv = new IPTV("192.1.1.2",32,2048);
		iptv.PrintProperty();
	}

}
