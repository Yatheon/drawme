package com.fish.drawme.common;

import org.json.simple.JSONObject;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Server extends Remote {
    void broadcastDrawing(String clientID, JSONObject data) throws RemoteException;
    String connect(Client client) throws RemoteException;
    void disconnect(String uniqueID) throws RemoteException;
}
