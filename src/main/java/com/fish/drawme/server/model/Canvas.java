package com.fish.drawme.server.model;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Iterator;


public class Canvas {

    private JSONArray figures;

    private String canvasID;

    public Canvas(JSONObject canvas){
        canvasID = (String)canvas.get("canvasID");
        figures = new JSONArray();
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
