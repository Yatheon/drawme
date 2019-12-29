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
    private String canvasID;
    private CanvasController controller;
    private boolean inCanvas = false;
    private boolean offline = false;
    private Stage primaryStage;
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
        this.primaryStage = primaryStage;
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
        }catch (Exception e){
            System.out.println("Offline mode activated");
            offline = true;
        }

    }
    /*
    Properly disconnects the system. Tells the server that we disconnected and exits the RMI application.
     */
    public void disconnect(){
        try{
            if(server!=null && inCanvas && !offline)
            server.disconnect(clientID);
        }catch (RemoteException e){
            e.printStackTrace();
        }
        System.exit(0);
    }
    //Here we tell the server that we drew something
    public void broadcastDrawing(JSONObject data){
        try{
            if(!offline)
            server.draw(clientID, data);
        }catch (RemoteException e){
            e.printStackTrace();
        }
    }

    /**
     * Called when the client joins a existing canvas
     * @param canvasID, The ID of the canvas that the client wants to join
     * @return JSONObject containing the canvas paintings.
     */
    public JSONObject joinCanvas(String canvasID){
        if(!offline)
        try{
            JSONObject serverResponse = server.connect(this, canvasID);

            clientID = (String)serverResponse.get("clientID");
            this.canvasID = canvasID;
            return(JSONObject) serverResponse.get("canvas");

        }catch (RemoteException e){
            System.out.println("Canvas does not exist");
            return null;
        }
        return null;
    }

    /**
     * Called when the client creates a new canvas
     */
    public void createNewCanvas(){
        if(!offline)
        try{
            JSONObject serverResponse = server.connect(this);
            clientID = (String)serverResponse.get("clientID");
            canvasID = (String)serverResponse.get("canvasID");
        }catch (RemoteException e){
            e.printStackTrace();
            canvasID ="Offline";
            offline = true;
        }
        else{
            canvasID ="Offline";
        }
    }

    /**
     * Leave the canvas. Tells the server that the client has left the canvas that he was in
     */
    public void leaveCanvas(){
        if(!offline)
        try{
            server.disconnect(clientID);
        }catch (RemoteException e){
            e.printStackTrace();
            offline = true;
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

    @Override
    public boolean ping() throws RemoteException {
        return true;
    }

    public String getCanvasID(){
        return canvasID;
    }
    public void setCanvasBoolean(){
        inCanvas=!inCanvas;
    }
}
