package com.fish.drawme.server.start;

import com.mongodb.client.*;
import org.bson.Document;

public class Start {

    public static void main(String[] args) {
        MongoClient mongoClient = MongoClients.create();
        MongoDatabase database = mongoClient.getDatabase("drawDB");
        MongoCollection<Document> collection = database.getCollection("canvases");
      //  Document doc = new Document("canvasID", "343422")
      //          .append("point", new Document("x", 203).append("y", 102).append("colour", "#FFF").append("size", 100));

      //  collection.insertOne(doc);

        MongoCursor<Document> cursor = collection.find().iterator();
        try {
            while (cursor.hasNext()) {
                System.out.println(cursor.next().toJson());
            }
        } finally {
            cursor.close();
        }

    }
}
