package com.example.promptsharepro;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoDBExample {
    public static void insertDocument() {
        MongoClient mongoClient = MongoDBConnection.getMongoClient();
        MongoDatabase database = mongoClient.getDatabase("test");
        MongoCollection<Document> collection = database.getCollection("testcollection");

        Document document = new Document("name", "John Doe")
                .append("email", "johndoe@example.com")
                .append("age", 30);

        collection.insertOne(document);

        mongoClient.close();
    }
}