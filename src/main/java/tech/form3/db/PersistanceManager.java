package tech.form3.db;

import java.util.List;
import java.util.Optional;

public interface PersistanceManager<T> {

    Optional<List<T>> find();

    void create(T object);

    void update(T object);

    void remove(T object);

    Optional<T> findById(String id);

}