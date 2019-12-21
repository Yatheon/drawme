package com.fish.drawme.server.controller;

import com.fish.drawme.common.Client;
import com.fish.drawme.common.Server;
import com.fish.drawme.server.model.User;
import com.fish.drawme.server.model.UserHandler;
import org.json.simple.JSONObject;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

public class Controller extends UnicastRemoteObject implements Server {
    final UserHandler userHandler = new UserHandler();
    public Controller() throws RemoteException {
    }


    public JSONObject connect(Client client, JSONObject canvasID)throws RemoteException{
        try {
            return userHandler.joinCanvas(client, canvasID);
        }
        catch (NoSuchFieldException nsfe){
            throw new RemoteException();
        }
    }
    public JSONObject newCanvas(Client client) throws RemoteException{

    }
    public void draw(String token, JSONObject cord) throws RemoteException{

    }
    public void changeColour(String token, JSONObject rgb) throws RemoteException{

    }
    public void changeSize(String token, JSONObject size) throws RemoteException{

    }
    public void removeCanvas(JSONObject canvasID) throws RemoteException{

    }
    public void disconnect(String token) throws RemoteException{

    }
}
