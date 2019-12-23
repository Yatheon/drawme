package com.fish.drawme.server.controller;

import com.fish.drawme.common.Client;
import com.fish.drawme.common.Server;
import com.fish.drawme.server.model.User;
import com.fish.drawme.server.model.UserHandler;
import org.json.simple.JSONObject;

import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

public class Controller extends UnicastRemoteObject implements Server {
    private final UserHandler userHandler = new UserHandler();
    public Controller() throws RemoteException {
    }


    public JSONObject connect(Client client, String canvasID)throws RemoteException{
        try {
            return userHandler.joinCanvas(client, canvasID);
        }
        catch (NoSuchObjectException nsoe){
            throw new RemoteException();
        }
    }
    public JSONObject connect(Client client) throws RemoteException{
        return userHandler.newCanvas(client);
    }
    public void draw(String token, JSONObject figure) throws RemoteException{
        userHandler.draw(token, figure);
    }
    public void removeCanvas(String canvasID) throws RemoteException{

    }
    public void disconnect(String token) throws RemoteException{
        userHandler.disconnect(token);
    }
}
