package com.inner;
import java.awt.*;
import java.awt.event.*;

public class WindwosEventEx03 extends Frame {
    
    public WindwosEventEx03() {        // 생성자 추가
        super("Event03");
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent arg0) {  // Closed → Closing 수정
                System.exit(0);
            }
        });
        setSize(400, 300);
        setVisible(true);              // 화면 표시 추가
    }
    
    public static void main(String[] args) {
        new WindwosEventEx03();        // 생성자 호출로 수정
    }
}