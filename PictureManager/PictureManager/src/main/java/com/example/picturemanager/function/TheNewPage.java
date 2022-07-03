package com.example.picturemanager.function;

import com.example.picturemanager.HelloApplication;
import com.example.picturemanager.tree.Tree;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;


public class TheNewPage {
    private final BorderPane borderPane = new BorderPane();
    private final ScrollPane scrollPane = new ScrollPane();
    private URL pagecss;
    private String imagePath;
    private String imageName;
    private final Label imageLabel = new Label();
    private final HBox hBox = new HBox();//水平按钮栏
    private final SecondPage secondPage = new SecondPage();
    private double scale =1;//图片大小比例
    private static boolean autoPlay = false;
    ImageView imageView;
    Stage imageStage;

    //构造函数
    public TheNewPage(String imagePath, String imageName){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        //todo 记得改成newpage
        pagecss = fxmlLoader.getClassLoader().getResource("newPage.css");
        this.imagePath = imagePath;
        this.imageName = imageName;
        imageStage = new Stage();
        imageStage.setTitle(imageName);
        SecondPage secondPage = new SecondPage();
        initImage(imagePath);
        addBottom();//将各个按钮add到界面中
        borderPane.setPrefSize(secondPage.getWidth(),secondPage.getHeight());
        borderPane.getStylesheets().add(pagecss.toExternalForm());
        imageStage.setScene(new Scene(borderPane,secondPage.getWidth(),secondPage.getHeight()));
        imageStage.initModality(Modality.NONE);//能够返回原窗口
        imageStage.show();
    }

    private void initImage(String imagePath){
        Image image = new Image(imagePath);
        imageView = new ImageView(image);

        imageView.setFitWidth(secondPage.getWidth()-100);
        imageView.setFitHeight(secondPage.getHeight()-150);
        imageView.setPreserveRatio(true);//保持缩放比例

        borderPane.setCenter(scrollPane);
        showImage(imageView);
    }

    //显示图片
    public void showImage(ImageView imageView){

        imageLabel.setPrefSize(secondPage.getWidth()-100,secondPage.getHeight()-150);
        imageLabel.setAlignment(Pos.BASELINE_CENTER);
        imageLabel.setGraphic(imageView);
        scrollPane.setContent(imageLabel);
        scrollPane.setPadding(new Insets(0,50,0,50));
        scrollPane.setHvalue(0.5);
        scrollPane.setVvalue(0.5);
    }

    public void addBottom(){
        hBox.setPrefSize(secondPage.getWidth()-100,100);
        hBox.setSpacing(20);
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().clear();

        //实现各种功能
        Button enlargeButton = new Button("放大");
        Button narrowButton = new Button("缩小");
        Button moveLeftButton = new Button("<--");
        Button moveRightButton = new Button("-->");
        Button auto = new Button("自动播放");


        //各按钮大小
        auto.setPrefSize(60,60);
        enlargeButton.setPrefSize(60,60);
        narrowButton.setPrefSize(60,60);
        moveLeftButton.setPrefSize(60,60);
        moveRightButton.setPrefSize(60,60);

        //将各个按钮add到界面中
        hBox.getChildren().addAll(moveLeftButton,enlargeButton,narrowButton,moveRightButton,auto);

        //创建时间轴，以便后续调用
        Timeline timeline=new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(true);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(1000), actionEvent -> moveRight());//关键帧！！！
        timeline.getKeyFrames().add(keyFrame);

        //自动播放功能
        auto.setOnAction(actionEvent -> {
            if(autoPlay){
                autoPlay = false;
                auto.setText("自动播放");
            }
            else {
                autoPlay = true;
                auto.setText("停止播放");
            }
            if(autoPlay)
                timeline.play();
            else
                timeline.stop();
        });


        //各按钮的触发事件
        enlargeButton.setOnAction(actionEvent -> enlargeImage());

        narrowButton.setOnAction(actionEvent -> narrowImage());

        moveLeftButton.setOnAction(actionEvent -> moveLeft());

        moveRightButton.setOnAction(actionEvent -> moveRight());

        borderPane.setBottom(hBox);
    }


    //放大
    private void enlargeImage(){
        scale += 0.23;
        setChangedSize();
    }

    //缩小
    private void narrowImage(){
        scale -= 0.23;
        if(scale <=0){
            scale +=0.23;
        }
        setChangedSize();
    }

    //设置好更改后的窗口尺寸,scale为比例
    private void setChangedSize() {
        //imageLabel.setPrefSize((secondPage.getWidth()-100)* scale,(secondPage.getHeight()-150)* scale);
        imageView.setFitWidth((secondPage.getWidth()-100)* scale);
        imageView.setFitHeight((secondPage.getHeight()-150)* scale);
        scrollPane.setHvalue(0.5);
        scrollPane.setVvalue(0.5);
        //borderPane.setCenter(imageLabel);//居中
    }

    private void moveLeft(){


        int cur=0;
        File file = new File(imagePath.substring(5));//去掉“file：”
        //System.out.println(imagePath);
        File fileParent = new File(file.getParent());

        String path = fileParent.getAbsolutePath();//获得点击的目录的名字
        PictureSearcher picture=new PictureSearcher(path);
        Tree tmp=picture.PictureCounter(new Tree(path));
        Tree tmps[]=tmp.GetChildren();//获得图片数组

        for(int i =0;i<tmp.GetNumOfChildren();i++){
            //System.out.println(i);
            String pictureName=(String) tmps[i].GetData();
            if(pictureName.equals(imageName))
            {
                cur=i;
            }
        }
        if(cur==0) {
            cur= tmp.GetNumOfChildren();
        }
        imageView = new ImageView(new Image("File:"+tmps[cur-1].GetWholeName()));
        imageView.setFitWidth(secondPage.getWidth()-100);
        imageView.setFitHeight(secondPage.getHeight()-150);
        imageView.setPreserveRatio(true);//保持缩放比例
        imagePath = "File:"+tmps[cur-1].GetWholeName();
        imageName=(String) tmps[cur-1].GetData();
        imageStage.setTitle(tmps[cur-1].GetWholeName());
        showImage(imageView);
        scale =1;
        setChangedSize();
    }

    private void moveRight(){
        File file = new File(imagePath.substring(5));
        File fileParent = new File(file.getParent());

        String path = fileParent.getAbsolutePath();//获得点击的目录的名字
        PictureSearcher picture=new PictureSearcher(path);
        Tree tmp=picture.PictureCounter(new Tree(path));
        Tree tmps[]=tmp.GetChildren();//获得图片数组

        int cur=tmp.GetNumOfChildren()-1;
        for(int i =0;i<tmp.GetNumOfChildren();i++){
            String pictureName=(String) tmps[i].GetData();
            if(pictureName.equals(imageName))
            {
                cur=i;
            }
        }
        if(cur==tmp.GetNumOfChildren()-1) {
            cur=-1;

        }
        imageView = new ImageView(new Image("File:"+tmps[cur+1].GetWholeName()));
        imageView.setPreserveRatio(true);//保持缩放比例
        imagePath = "File:"+tmps[cur+1].GetWholeName();
        imageName=(String) tmps[cur+1].GetData();
        imageStage.setTitle(tmps[cur+1].GetWholeName());
        showImage(imageView);
        scale =1;
        setChangedSize();
    }
}
