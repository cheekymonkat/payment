package tech.form3.resources;

import com.codahale.metrics.annotation.Timed;
import tech.form3.db.PersistanceManager;
import tech.form3.model.Data;
import tech.form3.model.Links;
import tech.form3.model.Payment;
import tech.form3.model.PaymentError;
import tech.form3.util.IdGenerator;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

import static tech.form3.resources.Api.VERSION;

@Path(VERSION)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CreateRestController {

    private final String hostUrl;
    private final PersistanceManager paymentService;
    private final IdGenerator idGenerator;

    public CreateRestController(final String hostUrl, final PersistanceManager paymentService, final IdGenerator idGenerator) {
        this.hostUrl = hostUrl;
        this.paymentService = paymentService;
        this.idGenerator = idGenerator;
    }

    @POST
    @Path("/")
    @Timed
    public Response createPayment(@Valid final Payment payment) {

        try {

            payment.setId(idGenerator.generate());

            paymentService.create(payment);

            Optional<Payment> newPayment = paymentService.findById(payment.getId());

            if (newPayment.isPresent()) {
                Data data = Data.builder()
                        .data(newPayment.get())
                        .links(Links.builder()
                                .self(hostUrl + Api.VERSION)
                                .build())
                        .build();

                return Response.status(Response.Status.CREATED).entity(data)
                        .build();
            }

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new PaymentError("Payment was not created."))
                    .build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new PaymentError("Unable to process your request."))
                    .build();
        }
    }
}
