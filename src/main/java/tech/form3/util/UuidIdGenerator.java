package tech.form3.util;

import java.util.UUID;

public class UuidIdGenerator implements IdGenerator {

    @Override
    public String generate() {
        return UUID.randomUUID().toString();
    }
}
