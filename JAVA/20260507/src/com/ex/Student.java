package com.ex;

public class Student {
    private String name;
    private String gwa;
    private int num;
    private double avg;

    public Student(String name, String gwa, int num, double avg) {
        this.name = name;
        this.gwa = gwa;
        this.num = num;
        this.avg = avg;
    }

    public String getName() { return name; }
    public String getGwa()  { return gwa; }
    public int getNum()     { return num; }
    public double getAvg()  { return avg; }
}