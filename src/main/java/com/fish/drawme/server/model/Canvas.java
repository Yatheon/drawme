package com.fish.drawme.server.model;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public class Canvas {

    private JSONArray figures;

    private String canvasID;

    public Canvas(JSONObject canvas){
        canvasID = (String)canvas.get("canvasID");
        JSONParser parser = new JSONParser();
        try {
            figures = (JSONArray) parser.parse(canvas.get("figures").toString());
        }catch (Exception e){
           figures = new JSONArray();
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
