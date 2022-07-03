package com.example.picturemanager.function;


import java.awt.*;

public class SecondPage {
    private static double width;
    private static double height;

    SecondPage(){
        Dimension scrSize=Toolkit.getDefaultToolkit().getScreenSize();
        width = scrSize.getWidth()*0.7-100;
        height = scrSize.getHeight()*0.8-200;
        //System.out.println(width+" "+height);
    }


    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

}
