package tech.form3.resources;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.After;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import tech.form3.db.PersistanceManager;
import tech.form3.model.Data;
import tech.form3.model.Payment;
import tech.form3.util.UuidIdGenerator;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Optional;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static tech.form3.resources.Api.VERSION;

@RunWith(MockitoJUnitRunner.class)
public class UpdateRestControllerTest {

    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();
    private static final PersistanceManager<Payment> dao = (PersistanceManager<Payment>) mock(PersistanceManager.class);

    private static final String ID = "id";
    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource( new UpdateRestController(TestHelper.HOST,dao,  new UuidIdGenerator()))
            .build();

    @Test
    public void whenUpdate_returnUpdatedPaymentAndOK() throws IOException {
        Payment currentPayment = getNewPayment();

        Payment newPayment = getNewPayment();
        newPayment.setId(currentPayment.getId());
        newPayment.setOrganisationId("new organisation");

        when(dao.findById(currentPayment.getId())).thenReturn(Optional.of(currentPayment));

        Response response = resources.target(VERSION + "/" + currentPayment.getId()).request()
                .accept(MediaType.APPLICATION_JSON).put(Entity.entity(newPayment, MediaType.APPLICATION_JSON_TYPE));

        assertThat(response.getStatus()).isEqualTo(Response.Status.CREATED.getStatusCode());
        assertThat(response.readEntity(Data.class).getData()[0]).isEqualTo(newPayment);

        verify(dao).update(any(Payment.class));
    }

    @Test
    public void whenUpdate_returnBadRequest(){

        when(dao.findById(ID)).thenReturn(Optional.empty());

        Response response = resources.target(VERSION + "/" + ID).request()
                .accept(MediaType.APPLICATION_JSON).put(Entity.entity(Payment.builder().id(ID).build(), MediaType.APPLICATION_JSON_TYPE));

        assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
        assertThat(response.readEntity(Error.class).getMessage()).isNotNull();

        verify(dao,times(0)).update(any(Payment.class));
    }

    @Test
    public void whenUpdate_failValidationWhenInvalidRequest(){

        when(dao.findById(ID)).thenReturn(Optional.empty());

        Response response = resources.target(VERSION + "/" + ID).request()
                .accept(MediaType.APPLICATION_JSON).put(Entity.entity(new Payment(), MediaType.APPLICATION_JSON_TYPE));

        assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());

        verify(dao,times(0)).update(any(Payment.class));
    }

    @Test
    public void whenUpdate_returnServerErrorWhenFindFails(){

        when(dao.findById(ID)).thenThrow(new RuntimeException());

        Response response = resources.target(VERSION + "/" + ID).request()
                .accept(MediaType.APPLICATION_JSON).put(Entity.entity(Payment.builder().id(ID).build(), MediaType.APPLICATION_JSON_TYPE));

        assertThat(response.getStatus()).isEqualTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        assertThat(response.readEntity(Error.class).getMessage()).isNotNull();

        verify(dao,times(0)).update(any(Payment.class));
    }

    @Test
    public void whenUpdate_returnServerErrorWhenUpdateFails(){

        when(dao.findById(ID)).thenReturn(Optional.of(Payment.builder().build()));
        doThrow(new RuntimeException()).when(dao).update(any(Payment.class));

        Response response = resources.target(VERSION + "/" + ID).request()
                .accept(MediaType.APPLICATION_JSON).put(Entity.entity(Payment.builder().id(ID).build(), MediaType.APPLICATION_JSON_TYPE));

        assertThat(response.getStatus()).isEqualTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        assertThat(response.readEntity(Error.class).getMessage()).isNotNull();

        verify(dao).update(any(Payment.class));
    }

    @After
    public void tearDown(){
        reset(dao);
    }

    public Payment getNewPayment() throws IOException {
        Payment payment = MAPPER.readValue(fixture("fixtures/payment.json"), Payment.class);
        return payment;
    }

}
