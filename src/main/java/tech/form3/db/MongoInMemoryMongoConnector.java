package tech.form3.db;

import com.github.fakemongo.Fongo;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.mongojack.JacksonCodecRegistry;

public class MongoInMemoryMongoConnector<T> implements MongoConnector<T> {

    private final Fongo fongo = new Fongo("inMemoryServer");
    private final MongoDatabase testDb = fongo.getDatabase("testDb");


    @Override
    public MongoCollection<T> getCollection(final String tableName, final Class<T> type) {
        testDb.createCollection(tableName);
        MongoCollection<Document> documents = testDb.getCollection(tableName);
        JacksonCodecRegistry jacksonCodecRegistry = new JacksonCodecRegistry();
        jacksonCodecRegistry.addCodecForClass(type);
        return documents.withDocumentClass(type).withCodecRegistry(jacksonCodecRegistry);
    }

    @Override
    public boolean ping() {
        return testDb.listCollections() != null;
    }

}
