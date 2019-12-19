package com.fish.drawme.client.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CanvasController implements Initializable {
    @FXML
    private ColorPicker color;
    @FXML
    private TextField brushSize;
    @FXML
    private Canvas canvas;
    @FXML
    private HBox toolbar;

    boolean brushSelected = false;

    GraphicsContext brushTool;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Canvas is now loaded!");
        brushTool = canvas.getGraphicsContext2D();
        canvas.setOnMouseDragged(e->draw(e));

    }
    //Initiation needed to make the canvas resizing work
    public void initiateCanvas(){
        canvas.getScene().widthProperty().addListener(evt -> extendCanvas());
        canvas.getScene().heightProperty().addListener(evt -> extendCanvas());
    }
    //This method makes the canvas resize with window resizing.
    private void extendCanvas() {
        double width = canvas.getScene().getWidth();
        double height = canvas.getScene().getHeight();

        canvas.setWidth(width);
        canvas.setHeight(height-toolbar.getHeight());
    }
    //Draw function. Used to draw on the canvas when a mouse-drag event happens on it
    private void draw(MouseEvent e){
        if(brushSelected && !brushSize.getText().isEmpty()){
            double size = Double.parseDouble(brushSize.getText());
            double x = e.getX() - size/2;
            double y = e.getY() - size/2;
            brushTool.setFill(color.getValue());
            brushTool.fillRoundRect(x,y,size,size,size,size);
        }
    }
    //Used to leave the canvas window. Then the user is redirected to the menu window.
    @FXML
    private void exitCanvas(ActionEvent event){
        System.out.println("Exiting Canvas");
        try{
            //load the menu.fxml
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/menu.fxml"));
            //Extract the stage in use
            Stage originalStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            //Set a new scene to the stage. The new scene is the menu.fxml file.
            originalStage.setScene(new Scene(root, 600, 400));

        }catch (IOException e){
            e.printStackTrace();
        }

    }
    /*
    A action method for when the user clicks the "brush" button. Activates the brush, so that the user can draw on the canvas.
     */
    @FXML
    private void selectBrush(ActionEvent event){
        brushSelected = !brushSelected;

        if(brushSelected){
            System.out.println("!Brush Selected");
        }
        else {
            System.out.println("!Brush De-selected");
        }
    }
}