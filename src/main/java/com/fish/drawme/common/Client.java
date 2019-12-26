package com.fish.drawme.common;

import com.fish.drawme.client.controller.CanvasController;
import org.json.simple.JSONObject;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Client extends Remote {
    void receiveDrawingBroadcast(JSONObject data) throws RemoteException;
    void ping() throws RemoteException;

}
