package tech.form3.db;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import tech.form3.model.Payment;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.fest.assertions.Assertions.assertThat;

@RunWith(JUnit4.class)
public class MongoPaymentsDaoTest {

    public static final String ID = "ABC";
    public static final String NEXT_ID = "DEF";

    @Test
    public void whenBasicActions_ensureSuccess(){

        MongoPaymentsDao mongoPaymentsDao = new MongoPaymentsDao(new MongoInMemoryMongoConnector());

        Payment firstPayment = Payment.builder().id(ID).build();
        Payment secondPayment = Payment.builder().id(NEXT_ID).build();

        mongoPaymentsDao.create(firstPayment);

        Optional<Payment> isCreated = mongoPaymentsDao.findById(ID);
        assertThat(isCreated.isPresent());
        assertThat(isCreated.get().getId()).isEqualTo(ID);

        mongoPaymentsDao.create(secondPayment);

        Optional<List<Payment>> payments = mongoPaymentsDao.find();

        List<Payment> sorted = payments.get().stream().sorted(Comparator.comparing(Payment::getId)).collect(Collectors.toList());

        assertThat(sorted.get(0).getId()).isEqualTo(ID);
        assertThat(sorted.get(1).getId()).isEqualTo(NEXT_ID);

        mongoPaymentsDao.remove(sorted.get(0));

        Optional<Payment> isRemoved = mongoPaymentsDao.findById(ID);
        assertThat(isRemoved.isPresent()).isFalse();

        secondPayment.setOrganisationId("new org");
        mongoPaymentsDao.update(secondPayment);

        Optional<List<Payment>> morePayments = mongoPaymentsDao.find();

        assertThat(morePayments.get().size()).isEqualTo(1);
        assertThat(morePayments.get()).contains(secondPayment);


    }


}