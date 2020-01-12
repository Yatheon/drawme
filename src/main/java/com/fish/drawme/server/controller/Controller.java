package com.fish.drawme.server.controller;

import com.fish.drawme.common.Client;
import com.fish.drawme.common.Server;
import com.fish.drawme.server.model.CanvasHandler;
import com.fish.drawme.server.model.UserHandler;
import org.json.simple.JSONObject;

import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Controller extends UnicastRemoteObject implements Server {
    private UserHandler userHandler;
    private CanvasHandler canvasHandler;
    public Controller(UserHandler userHandler, CanvasHandler canvasHandler) throws RemoteException {
        this.userHandler = userHandler;
        this.canvasHandler = canvasHandler;
    }


    public JSONObject connect(Client client, String canvasID)throws RemoteException{
        try {
            JSONObject joinCanvas = new JSONObject();
            joinCanvas.put("canvas",canvasHandler.getCanvas(canvasID).toJSON());
            joinCanvas.put("clientID", userHandler.createUser(client, canvasID));
            return joinCanvas;
        }
        catch (NoSuchObjectException nsoe){
            throw new RemoteException();
        }
    }
    public JSONObject connect(Client client) throws RemoteException{
        return userHandler.newCanvas(client, canvasHandler.newCanvas().getCanvasID());
    }
    public void draw(String token, JSONObject figure) throws RemoteException{
        userHandler.broadcastFigure(token, figure);
        canvasHandler.addFigure(userHandler.getCanvasID(token), figure);
    }
    public void removeCanvas(String canvasID) throws RemoteException{

    }
    @Override
    public void disconnect(String token) throws RemoteException{
        String canvasID =  userHandler.disconnect(token);
        canvasHandler.saveCanvas(canvasID);
        if (userHandler.canvasIsEmpty(canvasID))canvasHandler.forget(canvasID);
    }
}
