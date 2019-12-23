package com.fish.drawme.client.network;

import com.fish.drawme.client.controller.CanvasController;
import com.fish.drawme.client.controller.MenuController;
import com.fish.drawme.common.Client;
import com.fish.drawme.common.Server;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import static com.fish.drawme.common.Constants.SERVER_HOST_ADDRESS;
import static com.fish.drawme.common.Constants.SERVER_NAME_IN_REGISTRY;

public class Network extends UnicastRemoteObject implements Client {
    private Server server;
    private String clientID;
    private CanvasController controller;
    public Network(Stage primaryStage) throws RemoteException{
        connectToServer();
        try{
            setStage(primaryStage);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    /*
    Set the javaFX stage. It starts by loading the menu.fxml file.
     */
    private void setStage(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/menu.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("DrawMe");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.setOnCloseRequest(e->{
            disconnect();
        });
        primaryStage.show();
        ((MenuController) loader.getController()).setClient(this);
    }
    /**
     * Creates a Remote object of the server that the client can use to communicate with
     * Also give the server a object of itself, so that the server can initiate communication with the client.
     */
    private void connectToServer(){
        try{
            server = (Server) Naming.lookup(
                    "//" + SERVER_HOST_ADDRESS + "/" + SERVER_NAME_IN_REGISTRY);

            JSONObject fish = new JSONObject();
            JSONObject clientidentity;
            clientidentity = server.connect(this, fish );
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    /*
    Properly disconnects the system. Tells the server that we disconnected and exits the RMI application.
     */
    public void disconnect(){
        try{
            server.disconnect(clientID);
        }catch (RemoteException e){
            e.printStackTrace();
        }
        System.exit(0);
    }
    //Here we tell the server that we drew something
    public void broadcastDrawing(JSONObject data){
        try{
            server.draw(clientID, data);
        }catch (RemoteException e){
            e.printStackTrace();
        }
    }
    //Is called from the server when another client has drawn something
    @Override
    public void receiveDrawingBroadcast(JSONObject data) throws RemoteException {
        if(controller != null){
            controller.receiveDrawing(data);
        }
    }
    //Here we set the controller so that Network can communicate with the CanvasController
    public void setController(CanvasController controller){
        this.controller = controller;
    }
}
