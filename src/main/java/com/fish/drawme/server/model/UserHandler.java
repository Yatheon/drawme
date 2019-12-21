package com.fish.drawme.server.model;


import com.fish.drawme.common.Client;
import org.json.simple.JSONObject;

import javax.naming.AuthenticationException;

import java.util.*;




public class UserHandler {

   // private final Map<String, User> users = Collections.synchronizedMap(new HashMap<>());
    private final Map<String,Map<String, User>> canvases = Collections.synchronizedMap(new HashMap<>());



    public UserHandler(){
    }

    /**
     * Joins a existing canvas
     * @param client Client object with broadcast interface
     * @param canvasID Canvas that user wants to join
     * @return JSONObject with user token and canvas object
     * @throws Will throw exception if no canvas with provided ID exists
     */
    public JSONObject joinCanvas(Client client, JSONObject canvasID)throws NoSuchFieldException{
        String canvas = (String) canvasID.get("canvasID");
        Map<String, User> users = canvases.get(canvas);
        if (users != null){
            String token = UUID.randomUUID().toString();
            User user = new User();
            users.put(token, user);
            canvases.replace(canvas, users);
            JSONObject joinCanvas = new JSONObject();
            joinCanvas.put("token", token);
            joinCanvas.put("canvas", DB.GETCANVAS(canvas));
            return joinCanvas;
        }
        else{
            throw new NoSuchFieldException();
        }


    }
}
