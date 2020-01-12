package com.fish.drawme.server.model;


import com.fish.drawme.common.Client;
import org.json.simple.JSONObject;

import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.util.*;




public class UserHandler {
    private final Map<String, String> userToCanvasID = Collections.synchronizedMap(new HashMap<>());
    private final Map<String, Map<String, User>> canvasIDToUsers = Collections.synchronizedMap(new HashMap<>());


    public UserHandler(){
    }

    /**
     * Joins a existing canvas
     * @param client Client object with broadcast interface
     * @param canvasID Canvas that user wants to join
     * @return JSONObject with user token and canvas object
     * @throws NoSuchObjectException will throw exception if no canvas with provided ID exists
     */
    public synchronized JSONObject createUser(Client client, String canvasID){

            Map<String, User> users = canvasIDToUsers.get(canvasID);
            String clientID = UUID.randomUUID().toString();
            userToCanvasID.put(clientID, canvasID);

            User user = new User(client);
            users.put(clientID, user);
            canvasIDToUsers.put(canvasID, users);

            System.out.println("Canvas "+canvasID+" has been joined by User: "+clientID);
            for(Map.Entry<String,User> dick:users.entrySet()){
                System.out.println("User: "+dick.getKey());
            }

            JSONObject joinCanvas = new JSONObject();
            joinCanvas.put("clientID", clientID);
            return joinCanvas;


    }
    public JSONObject newCanvas(Client client, String canvasID){
        Map<String, User> users = new HashMap<>();
        String clientID = UUID.randomUUID().toString();
        userToCanvasID.put(clientID, canvasID);

        User user = new User(client);
        users.put(clientID, user);

        canvasIDToUsers.put(canvasID, users);
        System.out.println("Canvas created, Canvas ID: "+canvasID+", by User: "+clientID);
        JSONObject joinCanvas = new JSONObject();
        joinCanvas.put("clientID", clientID);
        joinCanvas.put("canvasID", canvasID);
        return joinCanvas;
    }
    public synchronized String disconnect(String token){
        String canvasID = userToCanvasID.remove(token);

        Map<String, User> users = canvasIDToUsers.get(canvasID);
        users.remove(token);
        System.out.println("Disconnection on Canvas "+canvasID+" with User "+token);

        canvasIDToUsers.put(canvasID,users);
        return canvasID;
    }
    public boolean canvasIsEmpty(String canvasID){
        Map<String, User> users = canvasIDToUsers.get(canvasID);
       return users.isEmpty();
    }
    public String getCanvasID(String token){
        return userToCanvasID.get(token);
    }
    public void broadcastFigure(String clientID, JSONObject figure){
        Map<String, User> users = canvasIDToUsers.get(userToCanvasID.get(clientID));
        for(Map.Entry<String, User> user : users.entrySet()){
            try {
                if(!user.getKey().equals(clientID)) user.getValue().sendFigure(figure);
            }catch (RemoteException re){
                disconnect(user.getKey());
            }
        }
    }

}
