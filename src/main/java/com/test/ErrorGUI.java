package com.test;

import lombok.extern.java.Log;

import javax.swing.*;
import java.awt.*;

@Log
public class ErrorGUI {
    public ErrorGUI(){
        String Title="Detect Error";
        JFrame frame=new JFrame(Title);
        Container container=frame.getContentPane();
        JLabel label1=new JLabel("发生了错误!");
        label1.setBounds(30,30,200,50);
        container.add(label1);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setBackground(Color.WHITE);
        frame.setVisible(true);
        frame.setSize(1000, 800);
    }
}
