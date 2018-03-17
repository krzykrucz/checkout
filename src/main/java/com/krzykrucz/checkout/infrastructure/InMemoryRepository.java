package com.krzykrucz.checkout.infrastructure;

import com.google.common.collect.Maps;
import com.krzykrucz.checkout.common.AggregateRootId;

import java.util.Map;
import java.util.Optional;

/**
 * @param <ID> aggregate root's id type
 * @param <T>  aggregate root type
 */
abstract class InMemoryRepository<ID extends AggregateRootId, T> {

    private final Map<ID, T> storage = Maps.newConcurrentMap();

    void store(ID id, T aggregate) {
        storage.put(id, aggregate);
    }

    Optional<T> fetchById(ID id) {
        if (!storage.containsKey(id)) {
            return Optional.empty();
        }
        return Optional.of(storage.get(id));
    }
}
