// Copyright 2016, Bruno Terkaly, All Rights Reserved
package com.terkaly.JavaMongoDB;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;
import com.google.gson.Gson;

public class App 
{
    public static void main( String[] args )
    {
        try {
            // Create a connection using mongoClient
            // 23.99.88.154 is obtained from the portal
            @SuppressWarnings("resource")
			MongoClient mongoClient = new MongoClient("13.89.57.52", 27017);
            
            // Get a connection to mydb
            DB db = mongoClient.getDB("mydb");
            
            // mydb has one or more collections, get "testCollection"
            DBCollection collection = db.getCollection("testCollection");
            
            // Create an empty object
            BasicDBObject empty = new BasicDBObject();
            
            // Clear out testCollection
            collection.remove( empty );
            
            // Acknowledges the write operation only
            // after committing the data to the journal
            mongoClient.setWriteConcern(WriteConcern.JOURNALED);
            
            // Here is the data format in JSON
            // {
            //   "name": "MongoDB",
            //   "type": "database",
            //   "count": 1,
            //   "info": {
            //         "x": 203,
            //         "y": 102
            //    }
            // }

            BasicDBObject doc = new BasicDBObject("name", "MongoDB")
                .append("type", "database")
                .append("count", 1)
                .append("info", new BasicDBObject("x", 203).append("y", 102));
            collection.insert(doc);
            
            DBObject myDoc = collection.findOne();
            System.out.println(myDoc);
            
            
                    
        } catch (Exception e) {
            e.printStackTrace();
        }    
    }
}













