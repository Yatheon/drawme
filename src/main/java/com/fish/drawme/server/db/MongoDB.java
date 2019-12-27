package com.fish.drawme.server.db;

import org.json.simple.JSONObject;

import java.util.Random;


public class MongoDB {


    public JSONObject getCanvas(String canvasID){
       return null;
    }
    public void saveCanvas(JSONObject canvas){

    }
    public String getNextID(){
        Random rand = new Random();
        return Integer.toString(rand.nextInt(100000));
    }
}
