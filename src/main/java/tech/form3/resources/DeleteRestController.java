package tech.form3.resources;

import com.codahale.metrics.annotation.Timed;
import tech.form3.db.PersistanceManager;
import tech.form3.model.Payment;
import tech.form3.model.PaymentError;
import tech.form3.util.IdGenerator;

import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

import static tech.form3.resources.Api.VERSION;

@Path(VERSION)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DeleteRestController {

    private final String hostUrl;
    private final PersistanceManager paymentService;
    private final IdGenerator idGenerator;

    public DeleteRestController(final String hostUrl, final PersistanceManager paymentService, final IdGenerator idGenerator) {
        this.hostUrl = hostUrl;
        this.paymentService = paymentService;
        this.idGenerator = idGenerator;
    }

    @DELETE
    @Path("/{id}")
    @Timed
    public Response deletePayment(@PathParam("id") @NotNull final String id) {
        try {

            Optional<Payment> paymentById = paymentService.findById(id);

            if (paymentById.isPresent()) {
                paymentService.remove(paymentById.get());
                return Response.status(Response.Status.NO_CONTENT)
                        .build();
            }

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new PaymentError("Payment did not exist"))
                    .build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new PaymentError("Unable to process your request."))
                    .build();
        }
    }

}
