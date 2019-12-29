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
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {
    Network client;
    @FXML
    TextField canvasIDField;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Menu is now loaded!");
    }

    /*
    When the user hits the "Create new Canvas" button
     */
    @FXML
    private void createNewCanvas(ActionEvent event){
        System.out.println("Creating new canvas!");
        client.createNewCanvas();
        switchToCanvasScene(event,null);
    }
    @FXML
    private void joinCanvas(ActionEvent event){
        String canvasID = canvasIDField.getText();
        System.out.println("Joining canvas "+canvasID);
        JSONObject canvas = client.joinCanvas(canvasID);
        //If the canvas does exist. Else, the scene is not switched.
        if(canvas!=null)
            switchToCanvasScene(event, canvas);
        else{
            badCanvasIDEntry();
        }

    }
    private void badCanvasIDEntry(){
        canvasIDField.setStyle("-fx-border-color: red;");
    }

    /**
     *
     * @param event, the button click event (join or createNewCanvas button)
     * @param canvas, If the client joins a canvas, then this parameter represents the canvas with its figures.
     *                Else it is null.
     */
    private void switchToCanvasScene(ActionEvent event, JSONObject canvas){
        try{
            //Load the Canvas FXML file from the resources folder.
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/canvas.fxml"));
            Parent root = loader.load();
            //Get the Stage object from the event. This is the object created in the Main class.
            Stage originalStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            //We set a new scene for the stage. This scene is the Canvas scene.
            originalStage.setScene(new Scene(root, 600, 400));

            //The following two lines of code make the canvas resizing work.
            CanvasController controller = loader.getController();
            controller.initiateCanvas();

            //Set the client for the CanvasController, so that it can communicate with the server(via the client)
            controller.setClient(client);

            //We paint the canvas if a canvas was joined
            if(canvas!=null)
            controller.paintCanvas(canvas);

            //We tell the Network object that we are now in the canvas page
            client.setCanvasBoolean();


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /*
    We have to set the client(Network) for the MenuController so that
    it can initiate communication with the server(via client)
     */
    public void setClient(Network client){
        this.client = client;
    }


}