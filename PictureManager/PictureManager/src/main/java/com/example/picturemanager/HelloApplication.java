package com.example.picturemanager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;

public class HelloApplication extends Application
{
    private static boolean isRight;// 是否处于右边界调整窗口状态
    private static boolean isBottomRight;// 是否处于右下角调整窗口状态
    private static boolean isBottom;// 是否处于下边界调整窗口状态
    private final static int RESIZE_WIDTH = 7;// 判定是否为调整窗口状态的范围与边界距离
    private final static double MIN_WIDTH = 800;// 窗口最小宽度
    private final static double MIN_HEIGHT = 600;// 窗口最小高度
    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void init() throws Exception {
        super.init();
        //Font font = Font.loadFont(getClass().getResourceAsStream("PERFORM GENERIC Regular.ttf"),12);
        //System.out.println(font.getFamily());
        System.out.println("initialized");
    }
    @Override
    public void start(Stage stage) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        URL cssurl = fxmlLoader.getClassLoader().getResource("ui.css");
        //Application.setUserAgentStylesheet(cssurl.toExternalForm());
        Scene scene = new Scene(fxmlLoader.load(), 1420, 780);
        stage.setTitle("电子图片管理程序");
        stage.setResizable(true);
        scene.getStylesheets().add(cssurl.toExternalForm());


        scene.setOnMouseMoved(event -> {
            event.consume();
            double x = event.getSceneX();
            double y = event.getSceneY();
            double width = stage.getWidth();
            double height = stage.getHeight();
            Cursor cursorType = Cursor.DEFAULT;// 鼠标光标初始为默认类型，若未进入调整窗口状态，保持默认类型
            // 先将所有调整窗口状态重置
            isRight = isBottomRight = isBottom = false;
            if (y >= height - RESIZE_WIDTH) {
                if (x <= RESIZE_WIDTH) {// 左下角调整窗口状态

                } else if (x >= width - RESIZE_WIDTH) {// 右下角调整窗口状态
                    isBottomRight = true;
                    cursorType = Cursor.SE_RESIZE;
                } else {// 下边界调整窗口状态
                    isBottom = true;
                    cursorType = Cursor.S_RESIZE;
                }
            } else if (x >= width - RESIZE_WIDTH) {// 右边界调整窗口状态
                isRight = true;
                cursorType = Cursor.E_RESIZE;
            }
            // 最后改变鼠标光标
            scene.setCursor(cursorType);
        });

        scene.setOnMouseDragged(event -> {
            double x = event.getSceneX();
            double y = event.getSceneY();
            // 保存窗口改变后的x、y坐标和宽度、高度，用于预判是否会小于最小宽度、最小高度
            double nextX = stage.getX();
            double nextY = stage.getY();
            double nextWidth = stage.getWidth();
            double nextHeight = stage.getHeight();

            if (isRight || isBottomRight) {// 所有右边调整窗口状态
                nextWidth = x;
            }
            if (isBottomRight || isBottom) {// 所有下边调整窗口状态
                nextHeight = y;
            }
            if (nextWidth <= MIN_WIDTH) {// 如果窗口改变后的宽度小于最小宽度，则宽度调整到最小宽度
                nextWidth = MIN_WIDTH;
            }
            if (nextHeight <= MIN_HEIGHT) {// 如果窗口改变后的高度小于最小高度，则高度调整到最小高度
                nextHeight = MIN_HEIGHT;
            }
            // 最后统一改变窗口的x、y坐标和宽度、高度，可以防止刷新频繁出现的屏闪情况
           /*
            scene.setX(nextX);
            scene.setY(nextY);
            scene.setWidth(nextWidth);
            scene.setHeight(nextHeight);
            if(!isBottom && !isBottomRight && !isRight)
            {
                scene.setX(event.getScreenX() - xOffset);
                scene.setY(event.getScreenY() - yOffset);
            }
            */
            stage.setX(nextX);
            stage.setY(nextY);
            stage.setWidth(nextWidth);
            stage.setHeight(nextHeight);
            if(!isBottom && !isBottomRight && !isRight)
            {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            }


        });

        scene.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        //!!!!这里的一定时fxmlloader.getclassloader不然会路径有问题
        System.out.println(fxmlLoader.getClassLoader().getResourceAsStream("PERFORM GENERIC Regular.ttf"));
        Font font = Font.loadFont(fxmlLoader.getClassLoader().getResourceAsStream("PERFORM GENERIC Regular.ttf"),12);
        Font font2 = Font.loadFont(fxmlLoader.getClassLoader().getResourceAsStream("CELingDHJW_Cu.TTF"),12);
        Font font3 = Font.loadFont(fxmlLoader.getClassLoader().getResourceAsStream("FZYunLTJW.TTF"),12);
      System.out.println(font3.getFamily());


/*

        Button smaller = new Button("  ");
        smaller.setId("smallBtn");
        smaller.setLayoutX(stage.getWidth()-100);
        smaller.setLayoutY(stage.getHeight()-30);
*/
        scene.setFill(null);

        stage.setScene(scene);

        stage.initStyle(StageStyle.TRANSPARENT);



        stage.show();

    }

    public static void main(String[] args)
    {
        //不要写到main里面，点开目录再加载，点开一层加载一层
        /*Tree menus=new Tree("我的电脑");//总目录
        PictureSearcher all=new PictureSearcher("C:\\");
        menus.AddChildren(all.PictureCounter(menus));;*/

        launch();
    }
}