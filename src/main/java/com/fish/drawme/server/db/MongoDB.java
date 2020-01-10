package com.fish.drawme.server.db;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.Document;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import org.slf4j.LoggerFactory;


public class MongoDB {
    MongoClient mongoClient;
    MongoDatabase database;
    public MongoDB(String database){
        mongoClient =  MongoClients.create();
        this.database  = mongoClient.getDatabase(database);
        ((LoggerContext) LoggerFactory.getILoggerFactory()).getLogger("org.mongodb.driver").setLevel(Level.ERROR);

    }
    public JSONObject getCanvas(String canvasID){
        System.out.println("canvasID to get: " + canvasID + "\n");//
        MongoCollection<Document> collection = database.getCollection("canvases");
        Document canvas = collection.find(Filters.eq("canvasID", canvasID)).first();

        if (canvas == null){
            System.out.println("Canvas not found\n"); //
            return null;
        }
        JSONParser parser = new JSONParser();
        try {
            System.out.println(canvas.toJson()+"\n"); //
            JSONObject json = (JSONObject) parser.parse(canvas.toJson());
            System.out.println("canvas found: \n "+ json.toJSONString()+ "\n");//
            return json;
        }catch (ParseException pe){
         pe.printStackTrace();
        }

        return null;
    }
    public void saveCanvas(JSONObject canvas){
        System.out.println(canvas.get("canvasID").toString());
        System.out.println(canvas.toJSONString());
        MongoCollection<Document> collection = database.getCollection("canvases");
        Document.parse(canvas.toJSONString());
        ReplaceOptions replaceOptions = new ReplaceOptions();
        replaceOptions.upsert(true);
        collection.replaceOne(Filters.eq("canvasID", canvas.get("canvasID")),   Document.parse(canvas.toJSONString()), replaceOptions);

    }
    public String getNextID(){
        MongoCollection<Document> collection = database.getCollection("canvasID");
        int idString;
        try {
            Document id = collection.find().first();
            System.out.println("ID: "+ id.toJson()+ "\n"); //
            idString= (int) id.get("currentID");
            idString+=1;
            id.replace("currentID", idString);
            ReplaceOptions replaceOptions = new ReplaceOptions();
            replaceOptions.upsert(true);
            collection.replaceOne(Filters.eq("currentID", idString-1), id, replaceOptions );
        }catch (NullPointerException npe){
            idString = 1;
            collection.insertOne(new Document("currentID", idString));
        }
        return Integer.toString(idString);
    }
}
