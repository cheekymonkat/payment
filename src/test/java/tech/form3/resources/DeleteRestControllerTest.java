package tech.form3.resources;


import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.After;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import tech.form3.db.PersistanceManager;
import tech.form3.model.Payment;
import tech.form3.util.UuidIdGenerator;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static tech.form3.resources.Api.VERSION;

@RunWith(MockitoJUnitRunner.class)
public class DeleteRestControllerTest {

    private static final PersistanceManager<Payment> dao = (PersistanceManager<Payment>) mock(PersistanceManager.class);

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new DeleteRestController(TestHelper.HOST, dao, new UuidIdGenerator()))
            .build();
    public static final String ID = "id";

    @Test
    public void whenDelete_RespondSuccessNoContent() {

        Payment payment = Payment.builder().id(ID).build();

        when(dao.findById(ID)).thenReturn(Optional.of(payment));

        Response response = resources.target(VERSION + "/" + ID).request().accept(MediaType.APPLICATION_JSON).delete();
        assertThat(response.getStatus()).isEqualTo(Response.Status.NO_CONTENT.getStatusCode());

        verify(dao).remove(payment);

    }

    @Test
    public void whenDelete_RespondFailBadRequest() {

        when(dao.findById(ID)).thenReturn(Optional.empty());

        Response response = resources.target(VERSION + "/" + ID).request().accept(MediaType.APPLICATION_JSON).delete();
        assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());

        verify(dao,times(0)).remove(any());

    }

    @Test
    public void whenDelete_RespondFailServerErrorWhenFindFails() {

        when(dao.findById(ID)).thenThrow(new RuntimeException());

        Response response = resources.target(VERSION + "/" + ID).request().accept(MediaType.APPLICATION_JSON).delete();
        assertThat(response.getStatus()).isEqualTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());

        verify(dao,times(0)).remove(any());

    }

    @Test
    public void whenDelete_RespondFailServerErrorWhenRemoveFails() {

        Payment payment = Payment.builder().id(ID).build();

        when(dao.findById(ID)).thenReturn(Optional.of(payment));
        doThrow(new RuntimeException()).when(dao).remove(payment);

        Response response = resources.target(VERSION + "/" + ID).request().accept(MediaType.APPLICATION_JSON).delete();
        assertThat(response.getStatus()).isEqualTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());

        verify(dao).remove(payment);

    }

    @After
    public void tearDown() {
        reset(dao);
    }


}
