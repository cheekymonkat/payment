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
import java.util.Arrays;
import java.util.Optional;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static tech.form3.resources.Api.VERSION;
import static tech.form3.resources.GetRestControllerTest.ID;

@RunWith(MockitoJUnitRunner.class)
public class GetAllRestControllerTest {

    private static final PersistanceManager<Payment> dao = (PersistanceManager<Payment>) mock(PersistanceManager.class);

    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();
    private static Data expectedPayments;

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource( new GetAllRestController(TestHelper.HOST,dao,  new UuidIdGenerator()))
            .build();


    @BeforeClass
    public static void init() throws IOException {
        expectedPayments = MAPPER.readValue(fixture("fixtures/payments.json"), Data.class);
    }

    @Test
    public void whenGet_returnPaymentsAndOK(){

        when(dao.find()).thenReturn(Optional.of(Arrays.asList(expectedPayments.getData())));

        Response response = resources.target(VERSION + "/").request().accept(MediaType.APPLICATION_JSON).get();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.readEntity(Data.class))
                .isEqualTo(expectedPayments);

        verify(dao).find();
    }

    @Test
    public void whenGet_returnBadRequestWhenEmpty(){

        when(dao.find()).thenReturn(Optional.empty());

        Response response = resources.target(VERSION + "/").request().accept(MediaType.APPLICATION_JSON).get();

        assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
        assertThat(response.readEntity(Error.class).getMessage()).isNotNull();

        verify(dao).find();
    }

    @Test
    public void whenGet_returnServerError(){

        when(dao.findById(eq(ID))).thenThrow(new RuntimeException());

        Response response = resources.target(VERSION + "/").request().accept(MediaType.APPLICATION_JSON).get();

        assertThat(response.getStatus()).isEqualTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        assertThat(response.readEntity(Error.class).getMessage()).isNotNull();

        verify(dao).find();
    }

    @After
    public void tearDown(){
        reset(dao);
    }


}
