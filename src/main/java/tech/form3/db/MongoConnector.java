package tech.form3.db;

import com.mongodb.client.MongoCollection;

public interface MongoConnector<T> {

    MongoCollection<T> getCollection(String tableName, Class<T> type);

    boolean ping();
}
