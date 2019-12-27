package com.fish.drawme.server.model;

import com.fish.drawme.server.db.MongoDB;
import org.bson.Document;
import org.json.simple.JSONObject;

import java.rmi.NoSuchObjectException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CanvasHandler {
    private final Map<String, Canvas> canvasIDToCanvas = Collections.synchronizedMap(new HashMap<>());
    private MongoDB db;

    public CanvasHandler(MongoDB db){
        this.db = db;
    }

    public Canvas getCanvas(String canvasID)throws NoSuchObjectException {
        Canvas canvas = canvasIDToCanvas.get(canvasID);
        if (canvas!= null)return canvas;

        JSONObject canvasJSON = db.getCanvas(canvasID);
        if (canvasJSON == null) throw new NoSuchObjectException("no");
        canvas = new Canvas(canvasJSON);
        canvasIDToCanvas.put(canvasID, canvas);
        return canvas;
    }
    public Canvas newCanvas(){
        JSONObject jsonObject = new JSONObject();
        String canvasID = db.getNextID();
        jsonObject.put("canvasID", canvasID);
        Canvas canvas = new Canvas(jsonObject);
        canvasIDToCanvas.put(canvasID, canvas);
        db.saveCanvas(jsonObject);
        return canvas;
    }
    public void saveCanvas(String canvasID){
        db.saveCanvas(canvasIDToCanvas.get(canvasID).toJSON());
    }
    public void forget(String canvasID){
        canvasIDToCanvas.remove(canvasID);
    }
    public void addFigure(String canvasID, JSONObject figure){

        canvasIDToCanvas.get(canvasID).addFigure(figure);
    }
}
