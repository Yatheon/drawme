package com.fish.drawme.server.model;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Iterator;


public class Canvas {

    private JSONArray figures;

    private String canvasID;

    public Canvas(JSONObject canvas){
        canvasID = (String)canvas.get("canvasID");
        JSONArray fig = (JSONArray) canvas.get("figures");
        Iterator<String> iterator = fig.iterator();
        while (iterator.hasNext()) {
            figures.add(iterator.next());
        }
    }
    public JSONObject toJSON(){
        JSONObject obj = new JSONObject();
        obj.put("canvasID", canvasID);
        obj.put("figures", figures);
        return obj;
    }
    public void addFigure(JSONObject figure){
        figures.add(figure);
    }
    public String getCanvasID() {
        return canvasID;
    }
}
