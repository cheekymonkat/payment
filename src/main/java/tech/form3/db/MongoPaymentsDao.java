package tech.form3.db;

import com.mongodb.client.MongoCollection;
import jersey.repackaged.com.google.common.collect.Lists;
import org.bson.Document;
import tech.form3.model.Payment;

import java.util.List;
import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;

public class MongoPaymentsDao implements PersistanceManager<Payment> {


    public static final String PAYMENTS_TABLE = "Payments";
    private MongoCollection<Payment> payments;

    public MongoPaymentsDao(final MongoConnector mongoConnector) {
        payments = mongoConnector.getCollection(PAYMENTS_TABLE, Payment.class);
    }

    @Override
    public Optional<List<Payment>> find() {
        return Optional.ofNullable(Lists.newArrayList(payments.find()));
    }


    @Override
    public void create(final Payment payment) {
        payments.insertOne(payment);
    }

    @Override
    public void update(final Payment payment) {
        payments.updateOne(eq("id", payment.getId()), new Document("$set", payment));
    }

    @Override
    public void remove(final Payment payment) {
        payments.deleteOne(eq("id", payment.getId()));
    }

    @Override
    public Optional<Payment> findById(final String id) {
        return Optional.ofNullable(payments.find(eq("id", id)).first());
    }

}
