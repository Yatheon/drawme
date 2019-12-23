package com.fish.drawme.server.model;


import com.fish.drawme.common.Client;
import org.json.simple.JSONObject;

import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.util.*;




public class UserHandler {
    private final Map<String, String> userToCanvasID = Collections.synchronizedMap(new HashMap<>());
    private final Map<String, Map<String, User>> canvasIDToUsers = Collections.synchronizedMap(new HashMap<>());
    private final Map<String, Canvas> canvasIDToCanvas = Collections.synchronizedMap(new HashMap<>());
    public UserHandler(){
    }

    /**
     * Joins a existing canvas
     * @param client Client object with broadcast interface
     * @param canvasID Canvas that user wants to join
     * @return JSONObject with user token and canvas object
     * @throws NoSuchObjectException will throw exception if no canvas with provided ID exists
     */
    public synchronized JSONObject joinCanvas(Client client, String canvasID)throws NoSuchObjectException{
        try{
            Canvas canvas = canvasIDToCanvas.get(canvasID);;
            if(canvas == null){
                canvas = new Canvas(canvasID);
                canvasIDToCanvas.put(canvasID, canvas);
            }

            String token = UUID.randomUUID().toString();
            userToCanvasID.put(token, canvasID);

            Map<String, User> users = canvasIDToUsers.get(canvasID);

            User user = new User(client);
            users.put(token, user);

            canvasIDToUsers.put(canvasID, users);

            JSONObject joinCanvas = new JSONObject();
            joinCanvas.put("token", token);
            joinCanvas.put("canvas", canvas);
            return joinCanvas;

        }catch (NoSuchObjectException nsoe){
            throw new NoSuchObjectException("No canvas with that id");
        }
    }
    public JSONObject newCanvas(Client client){



    }
    public synchronized void disconnect(String token){
        String canvasID = userToCanvasID.remove(token);
        canvasIDToCanvas.get(canvasID).saveCanvas();
        Map<String, User> users = canvasIDToUsers.get(canvasID);
        users.remove(token);
        if(users.isEmpty()){
            canvasIDToCanvas.remove(canvasID);
        }
        canvasIDToUsers.put(canvasID,users);
    }
    public void draw(String token, JSONObject figure){
        broadcastFigure(token, figure);
        canvasIDToCanvas.get(userToCanvasID.get(token)).addFigure(figure);
    }
    private void broadcastFigure(String token, JSONObject figure){
        Map<String, User> users = canvasIDToUsers.get(userToCanvasID);

        for(Map.Entry<String, User> user : users.entrySet()){
            try {
                user.getValue().sendFigure(figure);
            }catch (RemoteException re){
                disconnect(user.getKey());
            }
        }
    }

}
