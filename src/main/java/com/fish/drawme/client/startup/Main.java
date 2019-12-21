package com.fish.drawme.client.startup;

import com.fish.drawme.client.network.Network;
import com.fish.drawme.server.controller.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import static com.fish.drawme.common.Constants.CLIENT_NAME_IN_REGISTRY;
import static com.fish.drawme.common.Constants.SERVER_NAME_IN_REGISTRY;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        try{
            new Main().startRegistry();
            Naming.rebind(CLIENT_NAME_IN_REGISTRY, new Network(primaryStage));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void startRegistry() throws RemoteException {
        try {
            LocateRegistry.getRegistry().list();
        } catch (RemoteException noRegistryIsRunning) {
            LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
        }
    }
}