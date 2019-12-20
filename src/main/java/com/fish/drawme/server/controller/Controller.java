package com.fish.drawme.server.controller;

import com.fish.drawme.common.Client;
import com.fish.drawme.common.Server;
import org.json.simple.JSONObject;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

public class Controller extends UnicastRemoteObject implements Server {
    private HashMap<String,Client> activeClients = new HashMap<String,Client>();
    public Controller() throws RemoteException {
    }

    @Override
    public void broadcastDrawing(String clientID, JSONObject data) throws RemoteException {
        for(HashMap.Entry<String,Client> client:activeClients.entrySet()){
            if(!client.getKey().equals(clientID)){
                client.getValue().receiveDrawingBroadcast(data);
            }
        }
    }
    @Override
    public String connect(Client client) throws RemoteException {
        String uniqueID = UUID.randomUUID().toString();
        activeClients.put(uniqueID,client);
        System.out.println(uniqueID + " joined the canvas");
        return uniqueID;
    }
    @Override
    public void disconnect(String uniqueID) throws RemoteException {
        activeClients.remove(uniqueID);
        System.out.println(uniqueID + " left the canvas");
    }
}
