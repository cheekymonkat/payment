package tech.form3.resources;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import tech.form3.db.PersistanceManager;
import tech.form3.model.Data;
import tech.form3.model.Payment;
import tech.form3.util.UuidIdGenerator;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Optional;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static tech.form3.resources.Api.VERSION;

@RunWith(MockitoJUnitRunner.class)
public class GetRestControllerTest {

    private static final PersistanceManager<Payment> dao = (PersistanceManager<Payment>) mock(PersistanceManager.class);

    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();
    public static final String ID = "ABC";
    private static Payment expectedPayment;

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource( new GetRestController(TestHelper.HOST,dao, new UuidIdGenerator()))
            .build();


    @BeforeClass
    public static void init() throws IOException {
        expectedPayment= MAPPER.readValue(fixture("fixtures/payment.json"), Payment.class);
    }

    @Test
    public void whenGet_returnPaymentAndOKWithValidPaymentId(){

        when(dao.findById(eq(ID))).thenReturn(Optional.of(expectedPayment));

        Response response = resources.target(VERSION + "/" + ID).request().accept(MediaType.APPLICATION_JSON).get();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.readEntity(Data.class).getData()[0])
                .isEqualTo(expectedPayment);

        verify(dao).findById(ID);
    }

    @Test
    public void whenGet_returnNotFoundWhenInvalidId(){

        when(dao.findById(eq(ID))).thenReturn(Optional.empty());

        Response response = resources.target(VERSION + "/" + ID).request().accept(MediaType.APPLICATION_JSON).get();

        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
        assertThat(response.readEntity(Error.class).getMessage()).isNotNull();

        verify(dao).findById(ID);
    }

    @Test
    public void whenGet_returnServerError(){

        when(dao.findById(eq(ID))).thenThrow(new RuntimeException("error"));

        Response response = resources.target(VERSION + "/" + ID).request().accept(MediaType.APPLICATION_JSON).get();

        assertThat(response.getStatus()).isEqualTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        assertThat(response.readEntity(Error.class).getMessage()).isNotNull();

        verify(dao).findById(ID);
    }

    @After
    public void tearDown(){
        reset(dao);
    }


}
