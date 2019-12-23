package com.fish.drawme.server.model;

import com.fish.drawme.server.model.User;
import org.bson.Document;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.rmi.NoSuchObjectException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Canvas {

    private final Map<String, Document> canvases = Collections.synchronizedMap(new HashMap<>());
    private Document canvas;

    public Canvas(String canvasID)throws NoSuchObjectException{

    }

    public void removeCanvas(){

    }
    public JSONObject toJSON(){
        JSONParser parser = new JSONParser();
       try {
           JSONObject jsonObject = (JSONObject) parser.parse(canvas.toJson());
           return jsonObject;
       }catch (ParseException pe){
           pe.printStackTrace();
           return null;
       }
    }
    public void saveCanvas(){

    }
}
