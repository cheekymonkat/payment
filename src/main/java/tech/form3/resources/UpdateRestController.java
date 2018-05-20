package tech.form3.resources;

import com.codahale.metrics.annotation.Timed;
import tech.form3.db.PersistanceManager;
import tech.form3.model.Data;
import tech.form3.model.Links;
import tech.form3.model.Payment;
import tech.form3.model.PaymentError;
import tech.form3.util.IdGenerator;

import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

import static org.fest.assertions.Assertions.assertThat;
import static tech.form3.resources.Api.VERSION;

@Path(VERSION)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UpdateRestController {

    private final String hostUrl;
    private final PersistanceManager paymentService;
    private final IdGenerator idGenerator;

    public UpdateRestController(final String hostUrl, final PersistanceManager paymentService, final IdGenerator idGenerator) {
        this.hostUrl = hostUrl;
        this.paymentService = paymentService;
        this.idGenerator = idGenerator;
    }

    @PUT
    @Path("/{id}")
    @Timed
    public Response update(@PathParam("id") @NotNull final String id, @NotNull final Payment payment) {
        try {

            assertThat(payment.getId()).isNotNull();
            assertThat(id).isEqualTo(payment.getId());

            Optional<Payment> paymentById = paymentService.findById(id);

            if (paymentById.isPresent()) {

                //do some optional merging stuff in a helper class/service

                paymentService.update(payment);

                Data data = Data.builder()
                        .data(payment)
                        .links(Links.builder()
                                .self(hostUrl + Api.VERSION)
                                .build())
                        .build();

                return Response.status(Response.Status.CREATED)
                        .entity(data)
                        .build();
            }

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new PaymentError("Payment did not exist"))
                    .build();

        } catch (AssertionError e) {

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new PaymentError(e.getMessage()))
                    .build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new PaymentError("Unable to process your request."))
                    .build();
        }

    }

}
