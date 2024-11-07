package com.example.promptsharepro;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

public class MongoDBConnection {
    private static final String CONNECTION_STRING = "mongodb://mongo:wPtlHWqUmHUpGKInIannYebRZJzsejaL@junction.proxy.rlwy.net:21099";

    public static MongoClient getMongoClient() {
        return MongoClients.create(CONNECTION_STRING);
    }
}