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
import tech.form3.util.IdGenerator;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Optional;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static tech.form3.resources.Api.VERSION;

@RunWith(MockitoJUnitRunner.class)
public class CreateRestControllerTest {

    private static final PersistanceManager<Payment> dao = (PersistanceManager<Payment>) mock(PersistanceManager.class);

    private static final IdGenerator idGenerator = mock(IdGenerator.class);

    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();
    public static final String NEW_ID = "id";

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new CreateRestController(TestHelper.HOST, dao,  idGenerator))
            .build();


    public Payment getNewPayment() throws IOException {
        Payment payment = MAPPER.readValue(fixture("fixtures/payment.json"), Payment.class);
        payment.setId(null);
        return payment;
    }

    @Test
    public void whenCreate_returnNewPaymentAndOK() throws IOException {

        Payment newPayment = getNewPayment();

        when(idGenerator.generate()).thenReturn(NEW_ID);
        when(dao.findById(NEW_ID)).thenReturn(Optional.of(Payment.builder().id(NEW_ID).build()));

        Response response = resources.target(VERSION + "/").request()
                .accept(MediaType.APPLICATION_JSON).post(Entity.entity(newPayment, MediaType.APPLICATION_JSON_TYPE));

        assertThat(response.getStatus()).isEqualTo(Response.Status.CREATED.getStatusCode());
        assertThat(response.readEntity(Data.class).getData()[0].getId()).isEqualTo(NEW_ID);

        verify(dao).create(any(Payment.class));

    }

    @Test
    public void whenCreate_returnBadRequest() throws IOException {

        Payment newPayment = getNewPayment();

        when(idGenerator.generate()).thenReturn(NEW_ID);

        when(dao.findById(NEW_ID)).thenReturn(Optional.empty());

        Response response = resources.target(VERSION + "/").request()
                .accept(MediaType.APPLICATION_JSON).post(Entity.entity(newPayment, MediaType.APPLICATION_JSON_TYPE));

        assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
        assertThat(response.readEntity(Error.class).getMessage()).isNotNull();

        verify(dao).create(any(Payment.class));

    }

    @Test
    public void whenCreate_returnServerError() throws IOException {

        Payment newPayment = getNewPayment();

        when(idGenerator.generate()).thenReturn(NEW_ID);
        doThrow(new RuntimeException()).when(dao).create(newPayment);

        Response response = resources.target(VERSION + "/").request()
                .accept(MediaType.APPLICATION_JSON).post(Entity.entity(newPayment, MediaType.APPLICATION_JSON_TYPE));

        assertThat(response.getStatus()).isEqualTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        assertThat(response.readEntity(Error.class).getMessage()).isNotNull();

        verify(dao).create(any(Payment.class));
    }

    @After
    public void tearDown() {
        reset(dao);
    }


}
