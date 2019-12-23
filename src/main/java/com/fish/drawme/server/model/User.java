package com.fish.drawme.server.model;

import com.fish.drawme.common.Client;
import org.json.simple.JSONObject;

import java.rmi.RemoteException;

public class User {

    private Client client;

    public User(Client client){
        this.client = client;
    }
    public Client getClient() {
        return client;
    }
    public void userConnected()throws RemoteException{
        client.ping();
    }
    public synchronized void sendFigure(JSONObject figure)throws RemoteException{
        client.receiveDrawingBroadcast(figure);
    }
}
