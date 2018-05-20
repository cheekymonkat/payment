package tech.form3.health;

import com.codahale.metrics.health.HealthCheck;
import tech.form3.db.MongoConnector;

public class DatabaseHealthCheck extends HealthCheck {
    private final MongoConnector database;

    public DatabaseHealthCheck(final MongoConnector database) {
        this.database = database;
    }

    @Override
    protected Result check() {
        if (database.ping()) {
            return Result.healthy();
        }
        return Result.unhealthy("Can't ping database");
    }
}
