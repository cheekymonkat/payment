package tech.form3;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import tech.form3.db.MongoInMemoryMongoConnector;
import tech.form3.db.MongoPaymentsDao;
import tech.form3.db.PersistanceManager;
import tech.form3.health.DatabaseHealthCheck;
import tech.form3.resources.CreateRestController;
import tech.form3.resources.DeleteRestController;
import tech.form3.resources.GetAllRestController;
import tech.form3.resources.GetRestController;
import tech.form3.resources.UpdateRestController;
import tech.form3.util.IdGenerator;
import tech.form3.util.UuidIdGenerator;

public class Form3PaymentsApplication extends Application<Form3PaymentsConfiguration> {

    public static void main(final String[] args) throws Exception {
        new Form3PaymentsApplication().run(args);
    }

    @Override
    public String getName() {
        return "Form3 Payments";
    }

    @Override
    public void initialize(final Bootstrap<Form3PaymentsConfiguration> bootstrap) {
    }

    @Override
    public void run(final Form3PaymentsConfiguration configuration,
                    final Environment environment) {

        IdGenerator idGenerator = new UuidIdGenerator();
        MongoInMemoryMongoConnector mongoInMemoryMongoConnector = new MongoInMemoryMongoConnector();
        PersistanceManager mongoPaymentsDao = new MongoPaymentsDao(mongoInMemoryMongoConnector);

        environment.jersey().register(new CreateRestController(
                configuration.getHostUrl(),
                mongoPaymentsDao, idGenerator));

        environment.jersey().register(new GetAllRestController(
                configuration.getHostUrl(),
                mongoPaymentsDao, idGenerator));

        environment.jersey().register(new GetRestController(
                configuration.getHostUrl(),
                mongoPaymentsDao, idGenerator));

        environment.jersey().register(new DeleteRestController(
                configuration.getHostUrl(),
                mongoPaymentsDao, idGenerator));

        environment.jersey().register(new UpdateRestController(
                configuration.getHostUrl(),
                mongoPaymentsDao, idGenerator));


        environment.healthChecks().register("database", new DatabaseHealthCheck(mongoInMemoryMongoConnector));
        //You would potentially add endpoint healthchecks
    }



}
