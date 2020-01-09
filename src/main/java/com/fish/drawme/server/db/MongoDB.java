package com.fish.drawme.server.db;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.json.simple.JSONObject;

import java.util.Random;


public class MongoDB {
    MongoClient mongoClient;
    MongoDatabase database;
    public MongoDB(String database){
        mongoClient =  MongoClients.create();
        this.database  = mongoClient.getDatabase(database);
    }
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
