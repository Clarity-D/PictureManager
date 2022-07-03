package com.example.picturemanager.function;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageLabel {
    private final Label thePictureLabel = new Label();

    public ImageLabel(String imagePath, double width, double height){

        thePictureLabel.setPrefSize(width,height);
        thePictureLabel.setWrapText(true);
        thePictureLabel.setAlignment(Pos.BASELINE_CENTER);
        Image image = new Image(imagePath);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        imageView.setPreserveRatio(true);//保持缩放比例
        thePictureLabel.setGraphic(imageView);
    }


    public Label getThePictureLabel() {
        return thePictureLabel;
    }


}
