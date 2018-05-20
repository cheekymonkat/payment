package tech.form3.resources;

import com.codahale.metrics.annotation.Timed;
import tech.form3.db.PersistanceManager;
import tech.form3.model.Data;
import tech.form3.model.Links;
import tech.form3.model.Payment;
import tech.form3.model.PaymentError;
import tech.form3.util.IdGenerator;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

import static tech.form3.resources.Api.VERSION;

@Path(VERSION)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GetAllRestController {

    private final String hostUrl;
    private final PersistanceManager paymentService;
    private final IdGenerator idGenerator;

    public GetAllRestController(final String hostUrl, final PersistanceManager paymentService, final IdGenerator idGenerator) {
        this.hostUrl = hostUrl;
        this.paymentService = paymentService;
        this.idGenerator = idGenerator;
    }

    @GET
    @Path("/")
    @Timed
    public Response getPayments() {

        try {

            Optional<List<Payment>> payments = paymentService.find();

            if (payments.isPresent()) {
                Data data = Data.builder()
                        .data(payments.get())
                        .links(Links.builder()
                                .self(hostUrl + Api.VERSION)
                                .build())
                        .build();

                return Response.ok(data)
                        .build();
            }

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new PaymentError("The request failed."))
                    .build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new PaymentError("Unable to process your request."))
                    .build();
        }
    }

}
