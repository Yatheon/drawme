package com.fish.drawme.common;

import org.json.simple.JSONObject;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Server extends Remote {
    /**
     * Connects to a canvas with a client object.
     * @param client Client object that implements receiveDrawingBroadcast interface
     * @return JSONObject with client token and a canvas
     * @throws RemoteException Not yet defined
     */
    JSONObject connect(Client client, JSONObject canvasID) throws RemoteException;

    /**
     * Creates and connect to a canvas with client object.
     * @param client Client object that implements receiveDrawingBroadcast interface
     * @return JSONObject with client token, canvasID and a canvas
     * @throws RemoteException Not yet defined
     */
    JSONObject newCanvas(Client client) throws RemoteException;

    /**
     * Sends a x,y coordinate to the server which will be saved and broadcasted
     * to other clients connected on the same canvas
     * @param token token received when doing connect or newCanvas
     * @param cord x,y coordinates in a JSONObject
     * @throws RemoteException Not yet defined
     */
    void draw(String token, JSONObject cord) throws RemoteException;

    /**
     * Changes the current selected colour of a client
     * @param token token received when doing connect or newCanvas
     * @param rgb JSONObject wth a rgb value
     * @throws RemoteException Not yet defined
     */
    void changeColour(String token, JSONObject rgb) throws RemoteException;

    /**
     * Change the current size of the users brush
     * @param token token received when doing connect or newCanvas
     * @param size JSONObject with a size value
     * @throws RemoteException Not yet defined
     */
    void changeSize(String token, JSONObject size) throws RemoteException;

    /**
     * This one might cause problems if you remove
     * a canvas that is in use!
     */
    void removeCanvas(JSONObject canvasID) throws RemoteException;


    void disconnect(String token) throws RemoteException;

}
