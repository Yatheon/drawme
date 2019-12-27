package com.fish.drawme.server.model;


import com.fish.drawme.common.Client;
import org.json.simple.JSONObject;

import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.util.*;




public class UserHandler {
    private final Map<String, String> userToCanvasID = Collections.synchronizedMap(new HashMap<>());
    private final Map<String, Map<String, User>> canvasIDToUsers = Collections.synchronizedMap(new HashMap<>());

    private CanvasHandler canvasHandler;
    public UserHandler(CanvasHandler canvasHandler){
        this.canvasHandler = canvasHandler;
    }

    /**
     * Joins a existing canvas
     * @param client Client object with broadcast interface
     * @param canvasID Canvas that user wants to join
     * @return JSONObject with user token and canvas object
     * @throws NoSuchObjectException will throw exception if no canvas with provided ID exists
     */
    public synchronized JSONObject joinCanvas(Client client, String canvasID)throws NoSuchObjectException{

            Canvas canvas = canvasHandler.getCanvas(canvasID);
            Map<String, User> users = canvasIDToUsers.get(canvasID);

            String token = UUID.randomUUID().toString();
            userToCanvasID.put(token, canvasID);

            User user = new User(client);
            users.put(token, user);

            canvasIDToUsers.put(canvasID, users);

            JSONObject joinCanvas = new JSONObject();
            joinCanvas.put("token", token);
            joinCanvas.put("canvas", canvas.toJSON());
            return joinCanvas;


    }
    public JSONObject newCanvas(Client client){
        Canvas canvas = canvasHandler.newCanvas();
        Map<String, User> users = new HashMap<>();
        String canvasID = canvas.getCanvasID();

        String token = UUID.randomUUID().toString();
        userToCanvasID.put(token, canvasID);

        User user = new User(client);
        users.put(token, user);

        canvasIDToUsers.put(canvasID, users);

        JSONObject joinCanvas = new JSONObject();
        joinCanvas.put("token", token);
        joinCanvas.put("canvas", canvas.toJSON());
        return joinCanvas;
    }
    public synchronized void disconnect(String token){
        String canvasID = userToCanvasID.remove(token);
        canvasHandler.saveCanvas(canvasID);
        Map<String, User> users = canvasIDToUsers.get(canvasID);
        users.remove(token);
        if(users.isEmpty()){
            canvasHandler.forget(canvasID);
        }
        canvasIDToUsers.put(canvasID,users);
    }
    public void draw(String token, JSONObject figure){
        broadcastFigure(token, figure);
        canvasHandler.addFigure(userToCanvasID.get(token), figure);
    }
    private void broadcastFigure(String token, JSONObject figure){
        Map<String, User> users = canvasIDToUsers.get(userToCanvasID);

        for(Map.Entry<String, User> user : users.entrySet()){
            try {
                if(!user.getKey().equals(token)) user.getValue().sendFigure(figure);
            }catch (RemoteException re){
                disconnect(user.getKey());
            }
        }
    }

}
