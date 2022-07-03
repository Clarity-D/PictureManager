package com.example.picturemanager;

import com.example.picturemanager.Node.Triple;
import com.example.picturemanager.Node.Xbox;
import com.example.picturemanager.function.*;
import com.example.picturemanager.tree.Tree;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;


public class HelloController implements Initializable//实现接口Initializable!!!!!
{

    private int numOfSelectedPicture=0;

    private String presentTreeItem;

    //点击空白处取消选中
    private EventHandler cancelSelected=new EventHandler()
    {
        @Override
        public void handle(Event event)
        {
            if(!(((MouseEvent)event).getButton()==MouseButton.SECONDARY))
            {
                for(int i=0;i<pictureSaver.getChildren().size();i++)
                {
                    pictureSaver.getChildren().get(i).setStyle("-fx-background-color:linear-gradient(to right, rgba(66, 62, 62, 0.3), rgba(84, 86, 94, 0.3))");
                    ((Xbox)pictureSaver.getChildren().get(i)).isSelected=false;
                }
                numOfSelectedPicture=0;
                information.getChildren().clear();

                Label total3=new Label("共"+pictureSaver.getChildren().size()+"张图，已选中"+numOfSelectedPicture+"张图");
                information.getChildren().addAll(total3);
                information.setAlignment(Pos.CENTER); //居中
            }

        }
    };

    private static CopyPaste[] goal=new CopyPaste[10000];

    private int numOfCopyPaste=0;

    @FXML
    private Button Paste;

    @FXML
    private VBox information;

    @FXML
    private ScrollBar GeeBar;

    @FXML
    private Pane blankSpace;

    @FXML
    private FlowPane pictureSaver;

    @FXML
    private FlowPane showPane;

    @FXML
    private TreeView<File> menu;

    @FXML
    private VBox PreLabel;

    @FXML
    private VBox PanePane;

    @FXML
    private VBox paneLabel;
    @FXML
    private Button SecondButton;

    private URL cssurl;
    private URL cssuiurl;
    private ContextMenu ctm;

    //程序启动后调用，方法在类第一次初始化时调用，且仅调用一次。
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        // TODO Auto-generated method stub
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        cssurl = fxmlLoader.getClassLoader().getResource("dialog.css");
        cssuiurl = fxmlLoader.getClassLoader().getResource("ui.css");
        initTreeView();
        AddTreeItem();
        AddPicture();
        setMyPane();
    }

    private void initTreeView()//初始化TreeView
    {
        TreeItem<File> start = new TreeItem<>(new File("此电脑"));
        blankSpace.setPrefSize(1000,1000000);
        pictureSaver.setPrefSize(1000,1000000);
        menu.setRoot(start);//设置根目录
        File roots[]=File.listRoots();
        pictureSaver.setHgap(20);
        pictureSaver.setVgap(20);
        pictureSaver.setPadding(new Insets(50));

        //添加右键菜单
        ctm=new ContextMenu();

        MenuItem delect=new MenuItem("删除");
        delect.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                //弹出提示信息，是否要删除
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                //alert.initStyle(StageStyle.TRANSPARENT);
                Stage app = (Stage) menu.getScene().getWindow();
                app.getScene().setFill(Color.TRANSPARENT);
                alert.getDialogPane().getStylesheets().add(cssurl.toExternalForm());
                alert.titleProperty().set("警告");
                alert.headerTextProperty().set("是否要删除所选图片?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK)//如果选择确定
                {
                    for(int w=0;w<pictureSaver.getChildren().size();w++)
                    {
                        if(((Xbox)pictureSaver.getChildren().get(w)).isSelected)
                        {
                            String tmpPath=((ImageView)((Xbox)(pictureSaver.getChildren().get(w))).getChildren().get(0)).getImage().getUrl();
                            String tmpPath2=tmpPath.substring(5);
                            File deleTarget=new File(tmpPath2);
                            //System.out.println(deleTarget.getAbsolutePath());
                            pictureSaver.getChildren().remove(w);
                            deleTarget.delete();//从文件中删除
                            w--;//防止出现跳过某个图片的情况
                        }

                    }
                    information.getChildren().clear();
                    Label total=new Label("共"+pictureSaver.getChildren().size()+"张图，已选中0张图");
                    information.getChildren().addAll(total);
                    information.setAlignment(Pos.CENTER); //居中
                }

            }
        });//添加删除事件

        MenuItem copy=new MenuItem("复制");
        copy.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                numOfCopyPaste=0;
                for(int w=0;w<pictureSaver.getChildren().size();w++)
                {
                    if(((Xbox)pictureSaver.getChildren().get(w)).isSelected)
                    {
                        String tmpPath=((ImageView)((VBox)(pictureSaver.getChildren().get(w))).getChildren().get(0)).getImage().getUrl();
                        String tmpPath2=tmpPath.substring(5);
                        CopyPaste tmp=new CopyPaste(tmpPath2);
                        goal[numOfCopyPaste]=tmp;
                        numOfCopyPaste++;
                    }

                }

            }

        });//添加复制事件



        //MenuItem paste=new MenuItem("粘贴");
        MenuItem rename=new MenuItem("重命名");
        rename.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                int count=0;
                for(int w=0;w<pictureSaver.getChildren().size();w++)//确认是单选还是多选
                {
                    if(((Xbox)pictureSaver.getChildren().get(w)).isSelected)
                    {
                        count++;
                    }

                }
                if(count==1)//单选改名
                {
                    for(int w=0;w<pictureSaver.getChildren().size();w++)
                    {
                        if(((Xbox)pictureSaver.getChildren().get(w)).isSelected)
                        {
                            String tmpPath=((ImageView)((VBox)(pictureSaver.getChildren().get(w))).getChildren().get(0)).getImage().getUrl();
                            String tmpPath2=tmpPath.substring(5);
                            File reNameTarget=new File(tmpPath2);
                            TextInputDialog dialog = new TextInputDialog();

                            Stage app = (Stage) PanePane.getScene().getWindow();
                            System.out.println(app.toString());
                            app.getScene().setFill(Color.TRANSPARENT);
                            //dialog.initStyle(StageStyle.TRANSPARENT);

                            dialog.getDialogPane().getStylesheets().add(cssurl.toExternalForm());
                            dialog.setTitle("重命名");
                            dialog.setHeaderText("重命名");
                            dialog.setContentText("请输入新的名字:");
                            Optional<String> result = dialog.showAndWait();
                            if (result.isPresent())//如果选择确认
                            {
                                String newName=result.get();//输入的内容

                                String lastName=tmpPath2.substring(tmpPath2.lastIndexOf("."));//获得后缀
                                String tmpPath3=tmpPath2.substring(0,tmpPath2.lastIndexOf(reNameTarget.getName()));//获得名字前的路径
                                if(new File(tmpPath3+result.get()+lastName).exists())
                                {
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setTitle("警告");
                                    alert.setHeaderText("该名字已存在！");
                                    alert.showAndWait();
                                }
                                else
                                {
                                    reNameTarget.renameTo(new File(tmpPath3+newName+lastName));
                                    pictureSaver.getChildren().remove(w);
                                    ImageView newImageView=new ImageView(new Image("file:"+tmpPath3+newName+lastName,130, 130, true, false));
                                    Label newLabel=new Label(newName+lastName);
                                    //todo 形状
                                    newLabel.setPrefSize(100,50);
                                    newLabel.setAlignment(Pos.CENTER);//居中
                                    newLabel.setContextMenu(ctm);
                                    Xbox newBox=new Xbox(newImageView,newLabel);
                                    newBox.addEventFilter(MouseDragEvent.MOUSE_CLICKED, event2 ->
                                    {
                                        if(!(((MouseEvent)event2).getButton()==MouseButton.SECONDARY)&&  newBox.isSelected!=true)
                                        {
                                            newBox.isSelected=true;
                                            //if(!newBox.getStyle().equals("-fx-background-color:#66ccff"))
                                            //{
                                            //todo 颜色
                                            newBox.setStyle("-fx-background-color:linear-gradient(to right, rgba(185,208,197,0.3), rgba(156,166,234,0.3))");
                                            newBox.getStylesheets().add(cssuiurl.toExternalForm());
                                            information.getChildren().clear();
                                            numOfSelectedPicture++;
                                            //todo
                                            Label total2=new Label("共"+pictureSaver.getChildren().size()+"张图，已选中"+numOfSelectedPicture+"张图");
                                            information.getChildren().addAll(total2);
                                            information.setAlignment(Pos.CENTER); //居中

                                            //}
                                        }


                                    });
                                    pictureSaver.getChildren().add(w,newBox);//重新显示
                                }


                            }

                        }

                    }

                }
                else//多选改名
                {
                    Dialog<Triple> dialog = new Dialog<>();
                    //dialog.initStyle(StageStyle.TRANSPARENT);
                    Stage app = (Stage) menu.getScene().getWindow();
                    app.getScene().setFill(Color.TRANSPARENT);
                    dialog.getDialogPane().getStylesheets().add(cssurl.toExternalForm());
                    dialog.setTitle("重命名");
                    dialog.setHeaderText("请输入名称前缀、起始编号、编号位数");


                    //设置按钮
                    ButtonType loginButtonType = new ButtonType("确认", ButtonBar.ButtonData.OK_DONE);
                    dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

                    GridPane grid = new GridPane();
                    grid.setHgap(10);
                    grid.setVgap(10);
                    grid.setPadding(new Insets(20, 150, 10, 10));

                    TextField firstName = new TextField();
                    firstName.setPromptText("名称前缀");//框内的字
                    TextField beginNum = new TextField();
                    beginNum.setPromptText("起始编号");//框内的字
                    TextField NumOfDigits = new TextField();
                    NumOfDigits.setPromptText("编号位数");//框内的字



                    grid.add(new Label("名称前缀:"), 0, 0);//框前的字,i:列,i1:行
                    grid.add(firstName, 1, 0);
                    grid.add(new Label("起始编号:"), 0, 1);//框前的字,i:列,i1:行
                    grid.add(beginNum, 1, 1);
                    grid.add(new Label("编号位数:"), 0, 2);//框前的字,i:列,i1:行
                    grid.add(NumOfDigits, 1, 2);



                            /*// 根据是否输入名称前缀、起始编号、编号位数启用/禁用登录按钮
                            Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
                            loginButton.setDisable(true);//禁用按钮

                            firstName.textProperty().addListener((observable, oldValue, newValue) ->
                            {
                               boolean a=newValue.trim().isEmpty();//有输入时启用
                            });

                            beginNum.textProperty().addListener((observable, oldValue, newValue) ->
                            {
                                loginButton.setDisable(newValue.trim().isEmpty());
                            });*/

                    //将girdpane与dialog关联
                    dialog.getDialogPane().setContent(grid);

                    //设置鼠标光标初始在哪个框
                    Platform.runLater(() -> firstName.requestFocus());

                    //点击确认键时返回一个Triple
                    dialog.setResultConverter(dialogButton ->
                    {
                        if (dialogButton == loginButtonType)
                        {
                            return new Triple(firstName.getText(), beginNum.getText(),NumOfDigits.getText());
                        }
                        return null;
                    });

                    Optional<Triple> result = dialog.showAndWait();
                    //如果确认,改名
                    result.ifPresent(newName ->
                    {
                        String newName1=newName.getOne();//输入名称前缀
                        String newName2=newName.getTwo();//输入起始编号
                        String newName3=newName.getThree();//输入编号位数
                        int beginNum2 = Integer.parseInt(newName2);
                        int NumOfDigits3=Integer.parseInt(newName3);

                        for(int w=0;w<pictureSaver.getChildren().size();w++)
                        {
                            if(((Xbox)pictureSaver.getChildren().get(w)).isSelected)
                            {
                                String tmpPath=((ImageView)((VBox)(pictureSaver.getChildren().get(w))).getChildren().get(0)).getImage().getUrl();
                                String tmpPath2=tmpPath.substring(5);
                                File reNameTarget=new File(tmpPath2);
                                String lastName=tmpPath2.substring(tmpPath2.lastIndexOf("."));//获得后缀
                                String tmpPath3=tmpPath2.substring(0,tmpPath2.lastIndexOf(reNameTarget.getName()));//获得名字前的路径

                                String s2 =String.valueOf(beginNum2);
                                for(;s2.length()<NumOfDigits3;)
                                {
                                    String tmp="0"+s2;
                                    s2=tmp;
                                }

                                if(new File(tmpPath3+newName1+s2+lastName).exists())
                                {
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setTitle("警告");
                                    alert.setHeaderText("名字:"+newName1+s2+"已存在！");
                                    alert.showAndWait();
                                    break;
                                }
                                else
                                {
                                    reNameTarget.renameTo(new File(tmpPath3+newName1+s2+lastName));

                                    pictureSaver.getChildren().remove(w);
                                    ImageView newImageView=new ImageView(new Image("file:"+tmpPath3+newName1+s2+lastName,130, 130, true, false));
                                    Label newLabel=new Label(newName1+s2+lastName);
                                    newLabel.setPrefSize(100,50);
                                    newLabel.setAlignment(Pos.CENTER);//居中
                                    newLabel.setContextMenu(ctm);
                                    newLabel.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>()
                                    {
                                        @Override
                                        public void handle(ContextMenuEvent event)
                                        {
                                            if(numOfSelectedPicture==0)
                                            {
                                                for(int i=0;i<ctm.getItems().size();i++)
                                                    ctm.getItems().get(i).setDisable(true);
                                            }
                                            else
                                            {
                                                for(int i=0;i<ctm.getItems().size();i++)
                                                    ctm.getItems().get(i).setDisable(false);
                                            }
                                        }
                                    });
                                    Xbox newBox=new Xbox(newImageView,newLabel);
                                    newBox.addEventFilter(MouseDragEvent.MOUSE_CLICKED, event2 ->
                                    {
                                        if(!(((MouseEvent)event2).getButton()==MouseButton.SECONDARY)&&  newBox.isSelected!=true)
                                        {
                                            newBox.isSelected=true;
                                            //todo
                                            //if(!newBox.getStyle().equals("-fx-background-color:#66ccff"))
                                            //{
                                            newBox.setStyle("-fx-background-color:linear-gradient(to right, rgba(185,208,197,0.3), rgba(156,166,234,0.3))");
                                            newBox.getStylesheets().add(cssuiurl.toExternalForm());
                                            information.getChildren().clear();
                                            numOfSelectedPicture++;
                                            Label total2=new Label("共"+pictureSaver.getChildren().size()+"张图，已选中"+numOfSelectedPicture+"张图");
                                            information.getChildren().addAll(total2);
                                            information.setAlignment(Pos.CENTER); //居中

                                            //}
                                        }


                                    });
                                    pictureSaver.getChildren().add(w,newBox);//重新显示
                                    beginNum2++;
                                }

                            }
                        }



                    });


                }



            }


        });//添加重命名事件，只改前缀,可单选或多选改名
        ctm.getItems().addAll(delect,copy,rename);


        for(int i=0;i<roots.length;i++)
        {
            TreeItem<File> nextTreeItem= new TreeItem<>(roots[i]);
            start.getChildren().addAll(nextTreeItem);

        }
        menu.setCellFactory(new Callback<TreeView<File>, TreeCell<File>>()
        {
            @Override
            public TreeCell<File> call(TreeView<File> fileTreeView)
            {
                TreeCell<File> treeCell=new TreeCell<File>()
                {
                    @Override
                    protected void updateItem(File item,boolean empty)//修改TreeItem的文本或图片
                    {
                        super.updateItem(item,empty);
                        if(empty)
                        {
                            this.setGraphic(null);
                            this.setText(null);
                        }
                        if(item==null)
                        {
                            return;
                        }

                        this.setText(item.getName());
                        //if(item.getAbsolutePath().equals("C:\\") ||  item.getAbsolutePath().equals("D:\\"))
                        if(item.getAbsolutePath().matches("[A-Z]+:\\\\"))
                        {
                            this.setText(item.getAbsolutePath());
                        }
                    }
                };
                return treeCell;
            }
        });

        //点击空白处取消选中
        pictureSaver.addEventFilter(MouseDragEvent.MOUSE_CLICKED,cancelSelected);




        blankSpace.requestFocus();
        //按下ctrl是禁用单选，启用多选事件
        blankSpace.addEventHandler(KeyEvent.KEY_PRESSED,event->
        {
            if(event.getCode()==KeyCode.CONTROL)
            {
                //System.out.println("按");
                pictureSaver.removeEventFilter(MouseDragEvent.MOUSE_CLICKED,cancelSelected);//取消绑定事件
            }


        });
        //松开ctrl是启用单选，禁用多选事件
        blankSpace.addEventHandler(KeyEvent.KEY_RELEASED,event->
        {
            if(event.getCode()==KeyCode.CONTROL)
            {
                //System.out.println("松");
                pictureSaver.addEventFilter(MouseDragEvent.MOUSE_CLICKED,cancelSelected);//重新绑定事件
            }

        });
        //添加粘贴事件
        Paste.addEventFilter(MouseDragEvent.MOUSE_CLICKED,event ->
        {
            if(numOfCopyPaste!=0)
            {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.titleProperty().set("警告");
                alert.headerTextProperty().set("是否要把图片粘贴到该文件夹中?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK)//如果选择确定
                {
                    for(int u=0;u<numOfCopyPaste;u++)
                    {
                        goal[u].setTarget(presentTreeItem+"\\"+new File(goal[u].getSource()).getName());
                        goal[u].PasteTargetIsExisted();
                        goal[u].PasteFile();

                        //重绘
                        ImageView newImageView=new ImageView(new Image("file:"+goal[u].getTarget(),130, 130, true, false));
                        Label newLabel=new Label(new File(goal[u].getTarget()).getName());
                        newLabel.setPrefSize(100,50);
                        newLabel.setAlignment(Pos.CENTER);//居中
                        newLabel.setContextMenu(ctm);
                        newLabel.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>()
                        {
                            @Override
                            public void handle(ContextMenuEvent event)
                            {
                                if(numOfSelectedPicture==0)
                                {
                                    for(int i=0;i<ctm.getItems().size();i++)
                                        ctm.getItems().get(i).setDisable(true);
                                }
                                else
                                {
                                    for(int i=0;i<ctm.getItems().size();i++)
                                        ctm.getItems().get(i).setDisable(false);
                                }
                            }
                        });
                        Xbox newBox=new Xbox(newImageView,newLabel);
                        newBox.addEventFilter(MouseDragEvent.MOUSE_CLICKED, event2 ->
                        {
                            if(!(((MouseEvent)event2).getButton()==MouseButton.SECONDARY)&&  newBox.isSelected!=true)
                            {
                                newBox.isSelected=true;
                                //if(!newBox.getStyle().equals("-fx-background-color:#66ccff"))
                                //{
                                newBox.setStyle("-fx-background-color:linear-gradient(to right, rgba(185,208,197,0.3), rgba(156,166,234,0.3))");
                                newBox.getStylesheets().add(cssuiurl.toExternalForm());
                                information.getChildren().clear();
                                numOfSelectedPicture++;
                                Label total2=new Label("共"+pictureSaver.getChildren().size()+"张图，已选中"+numOfSelectedPicture+"张图");
                                information.getChildren().addAll(total2);
                                information.setAlignment(Pos.CENTER); //居中

                                //}
                            }


                        });
                        pictureSaver.getChildren().add(newBox);//重新显示
                    }
                    information.getChildren().clear();
                    Label total=new Label("共"+pictureSaver.getChildren().size()+"张图，已选中0张图");
                    information.getChildren().addAll(total);
                    information.setAlignment(Pos.CENTER); //居中
                }
            }
            else
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("警告");
                alert.setHeaderText("你还没有复制！");
                alert.showAndWait();
            }

        });

    }



    private void AddTreeItem()//每点开一层，就显示该层所有目录
    {
        menu.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
        {
            String path = newValue.getValue().getAbsolutePath();//获得点击的目录的名字
            if(newValue.isLeaf())//该目录还没被点开过
            {
                PictureSearcher nextChild=new PictureSearcher(path);
                Tree tmp=nextChild.MenuSearcher(new Tree(path));
                Tree tmps[]=tmp.GetChildren();
                for(int i=0;i<tmp.GetNumOfChildren();i++)
                {
                    File paths=new File(tmps[i].GetWholeName());
                    TreeItem<File> nextTreeItem= new TreeItem<>(paths);
                    newValue.getChildren().addAll(nextTreeItem);

                }

            }



        });
    }
    //private static CopyPaste tmpff;
    //需要动态调整flowpane和滚动条的大小
    private void AddPicture()//每点开一层，就显示该层所有图片
    {
        menu.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
        {
            presentTreeItem=newValue.getValue().getAbsolutePath();
            numOfSelectedPicture=0;
            pictureSaver.getChildren().clear();//清屏
            information.getChildren().clear();//清屏

            //添加滚动条,test
            //ScrollBar GeeBar=new ScrollBar();
            GeeBar.setOrientation(Orientation.VERTICAL);//把朝向设置为垂直
            GeeBar.setMin(0);//设置滚动条起始点
            GeeBar.setMax(1000000);//设置scrollBar对象的活动位置最大值
            GeeBar.setValue(0);//设置滚动条开始的位置
            GeeBar.setLayoutX(1400);//设置滚动条的位置
            GeeBar.setPrefHeight(780);//设置ScrollBar对象的初始化高度
            GeeBar.setUnitIncrement(50);//设置每滚一下下落多少

            GeeBar.valueProperty().addListener((ObservableValue<? extends Number> ov,
                                                Number old_val, Number new_val) -> {//scrollBar对象进行改变值事件监听处理机制
                pictureSaver.setLayoutY(-new_val.doubleValue());//当scrollBar对象被滑动时将容器对象的Y轴坐标设置为滑动后的逆向取值

            });

            //添加滚动事件监听
            pictureSaver.addEventFilter(ScrollEvent.SCROLL, event ->
            {
                if (event.getDeltaY() > 0)
                {
                    // 往上
                    GeeBar.decrement();

                }
                else
                {
                    // 往下
                    GeeBar.increment();
                }
                //pictureSaver.setLayoutY(GeeBar.getValue()*(-10));
            });

            //blankSpace.getChildren().addAll(GeeBar);


            String path = newValue.getValue().getAbsolutePath();//获得点击的目录的名字
            PictureSearcher picture=new PictureSearcher(path);
            Tree tmp=picture.PictureCounter(new Tree(path));
            Tree tmps[]=tmp.GetChildren();//获得图片数组

            //图片信息统计

            String res=String.format("%.2f",tmp.getAllSize()/1024.0/1024.0);
            Label total=new Label("共"+tmp.GetNumOfChildren()+"张图，已选中0张图"+"\n图片总大小为:"+res+"M");
            information.getChildren().addAll(total);
            information.setAlignment(Pos.CENTER); //居中

            //幻灯片播放
            SecondButton.setOnAction(actionEvent -> {
                if(tmps!=null) beginPlay(tmps[0]);
                else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("警告");
                    alert.setHeaderText("请打开一个有图片的目录！");
                    alert.showAndWait();
                }
            });

            for(int i=0;i<tmp.GetNumOfChildren();i++)
            {
                Image image = new Image("file:"+tmps[i].GetWholeName(), 130, 130, true, false);//b:保持原来的比例 b1?
                ImageView imageHouse = new ImageView(image);
                Label name=new Label((String)tmps[i].GetData());
                name.setPrefSize(130,50);
                name.setAlignment(Pos.CENTER); //居中

                name.setContextMenu(ctm);

                //右键打开菜单时发生的操作
                name.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>()
                {
                    @Override
                    public void handle(ContextMenuEvent event)
                    {
                        if(numOfSelectedPicture==0)
                        {
                            for(int i=0;i<ctm.getItems().size();i++)
                                ctm.getItems().get(i).setDisable(true);
                        }
                        else
                        {
                            for(int i=0;i<ctm.getItems().size();i++)
                                ctm.getItems().get(i).setDisable(false);
                        }
                    }
                });

                Xbox xbox=new Xbox(imageHouse,name);
                //单击选中变蓝,单选事件
                xbox.addEventFilter(MouseDragEvent.MOUSE_CLICKED, event ->
                {
                    if(!(((MouseEvent)event).getButton()==MouseButton.SECONDARY))
                    {

                        if(!xbox.isSelected)
                        {
                            xbox.isSelected=true;
                            xbox.setStyle("-fx-background-color:linear-gradient(to right, rgba(185,208,197,0.3), rgba(156,166,234,0.3))");
                            information.getChildren().clear();
                            numOfSelectedPicture++;
                            Label total2=new Label("共"+tmp.GetNumOfChildren()+"张图，已选中"+numOfSelectedPicture+"张图");
                            information.getChildren().addAll(total2);
                            information.setAlignment(Pos.CENTER); //居中

                            //todo dongde daima
                            String imagePath=((ImageView)xbox.getChildren().get(0)).getImage().getUrl();
                            String imageName=((Label)xbox.getChildren().get(1)).getText();
                            ImageLabel PLabel=new ImageLabel(imagePath,200,250);
                            PanePane.getChildren().clear();
                            //paneLabel.getChildren().clear();
                            //PanePane.getChildren().addAll(PreLabel);
                            PreLabel.getChildren().clear();//清屏
                            if(PreLabel!=null)
                                PreLabel.getChildren().addAll(PLabel.getThePictureLabel());

                            try {
                                ImageInformation PInformation=new ImageInformation(imagePath,imageName);
                                PanePane.getChildren().addAll(PInformation.getImageInformationLabel());
                            } catch (IOException e) {
                                e.printStackTrace();


                            }



                            PanePane.setAlignment(Pos.CENTER); //居中
                            PreLabel.setAlignment(Pos.TOP_CENTER); //居中
                        }
                    }


                });



                //双击事件
                xbox.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
                    if(mouseEvent.getClickCount()==2)
                    {
                        String imagePath=((ImageView)xbox.getChildren().get(0)).getImage().getUrl();
                        String imageName=((Label)xbox.getChildren().get(1)).getText();
                        //新窗口
                        new TheNewPage(imagePath,imageName);
                    }
                });


                pictureSaver.getChildren().addAll(xbox);

            }




        });


    }

    //从第一张图片开始播放幻灯片
    public void beginPlay(Tree p1)
    {
        new TheNewPage("File:"+p1.GetWholeName(), (String) p1.GetData());
    }
    public void onClos(ActionEvent actionEvent) {
        Stage app = (Stage) this.menu.getScene().getWindow();

        EventHandler opt = this.menu.getScene().getWindow().onCloseRequestProperty().get();
        if (opt != null) {
            opt.handle(new WindowEvent(app, WindowEvent.WINDOW_CLOSE_REQUEST));
        }
        else {
            app.close();
        }
    }

    public void onSmal(ActionEvent actionEvent) {
        Stage app = (Stage) this.menu.getScene().getWindow();
        app.setIconified(true);
    }

    public void ondrag(ActionEvent actionEvent){
        pictureSaver.setLayoutY(GeeBar.getValue()*(-10));
    }


    public void setMyPane()
    {
        showPane.setPrefSize(250,780);
//        showPane.setStyle("-fx-background-color:#3d2020");
//        showPane.setStyle("-fx-border-color:BLUE");
        showPane.setPadding(new Insets(12,13,14,15));
        showPane.setOrientation(Orientation.VERTICAL);
        showPane.setHgap(8);
        showPane.setVgap(5);
    }


}