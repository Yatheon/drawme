package com.fish.drawme.client.controller;

import com.fish.drawme.client.network.Network;
import com.fish.drawme.common.Client;
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
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Random;
import java.util.ResourceBundle;

import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;

public class CanvasController implements Initializable {
    Network client;
    @FXML
    private ColorPicker color;
    @FXML
    private TextField brushSize;
    @FXML
    private Text canvasID;
    @FXML
    private Canvas canvas;
    @FXML
    private HBox toolbar;

    private String id;

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
    private void draw(MouseEvent event){
        if(brushSelected && !brushSize.getText().isEmpty()){
            double size = Double.parseDouble(brushSize.getText());
            double x = event.getX() - size/2;
            double y = event.getY() - size/2;
            brushTool.setFill(color.getValue());
            brushTool.fillRoundRect(x,y,size,size,size,size);
            //We use a JSONObject as data for the drawing
            JSONObject data = new JSONObject();

            data.put("x",x);
            data.put("y",y);
            data.put("size",size);
            data.put("color",""+color.getValue());

            //Broadcast the drawing to the server via the client(Network) object
            client.broadcastDrawing(data);
        }
    }
    //Used to leave the canvas window. Then the user is redirected to the menu window.
    @FXML
    private void exitCanvas(ActionEvent event){
        System.out.println("Exiting Canvas");
        //Remove the controller object from the client(Network).
        client.leaveCanvas();
        client.setController(null);
        try{
            //load the menu.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/menu.fxml"));
            Parent root = loader.load();
            //Extract the stage in use
            Stage originalStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            //Set a new scene to the stage. The new scene is the menu.fxml file.
            originalStage.setScene(new Scene(root, 600, 400));
            ((MenuController)loader.getController()).setClient(client);

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
            System.out.println("Brush Selected");
        }
        else {
            System.out.println("Brush De-selected");
        }
    }
    /*
    We have to set the client(Network) for the CanvasController so that
    it can initiate communication with the server(via client)
    */
    public void setClient(Network client){
        this.client = client;
        client.setController(this);
        id = client.getCanvasID();
        canvasID.setText("ID: "+id);
    }
    /*
    Is called when the client receives a drawing broadcast from another client
     */
    public void receiveDrawing(JSONObject data){
        double x = (double) data.get("x");
        double y = (double) data.get("y");
        double size = (double) data.get("size");
        //Turn the hexadecimal representation of the color into a Color object
        brushTool.setFill(Color.web((String)data.get("color")));
        brushTool.fillRoundRect(x,y,size,size,size,size);
    }

    /*
    Mouse interaction with ID text
     */
    @FXML
    public void mouseEnteredID(MouseEvent event){
        canvasID.setFill(Color.web("#d6ccc6"));
    }
    @FXML
    public void mouseExitedID(MouseEvent event){
        canvasID.setFill(Color.web("#656565"));
    }
    @FXML
    public void mouseClickedID(MouseEvent event){
        StringSelection stringSelection = new StringSelection(id);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }
    @FXML
    public void mousePressedID(MouseEvent event){
        canvasID.setFill(Color.web("#e3e3e3"));
    }
}