package com.fish.drawme.common;

import org.json.simple.JSONObject;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Server extends Remote {
    /**
     * Connects to a canvas with a client object.
     * @param client Client object that implements receiveDrawingBroadcast interface
     * @return String of a canvas
     * @throws RemoteException Not yet defined
     */
    JSONObject connect(Client client, String canvasID) throws RemoteException;

    /**
     * Creates and connect to a canvas with client object.
     * @param client Client object that implements receiveDrawingBroadcast interface
     * @return JSONObject with client token, canvasID and a canvas
     * @throws RemoteException Not yet defined
     */
    JSONObject connect(Client client) throws RemoteException;

    /**
     * Sends a x,y coordinate to the server which will be saved and broadcasted
     * to other clients connected on the same canvas
     * @param cord  token received when doing connect or newCanvas and x,y coordinates in a JSONObject
     * @throws RemoteException Not yet defined
     */
    void draw(JSONObject cord) throws RemoteException;
    /**
     * This one might cause problems if you remove
     * a canvas that is in use!
     */
    void removeCanvas(String canvasID) throws RemoteException;


    void disconnect(String token) throws RemoteException;

}
