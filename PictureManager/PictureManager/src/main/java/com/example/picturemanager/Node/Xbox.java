package com.example.picturemanager.Node;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.net.URL;

public class Xbox extends VBox
{
    public boolean isSelected = false;


    public Xbox(ImageView imageHouse,Label name)
    {
        super();
        setId("xbox");

        //this.setStyle("-fx-background-color:#FFFFFF");
        this.setAlignment(Pos.CENTER);//居中
        this.getChildren().addAll(imageHouse,name);
    }

}
