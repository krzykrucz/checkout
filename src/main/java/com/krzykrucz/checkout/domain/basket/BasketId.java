package com.krzykrucz.checkout.domain.basket;

import com.krzykrucz.checkout.common.AggregateRootId;

import java.util.UUID;

public class BasketId extends AggregateRootId {

    private BasketId(UUID uuid) {
        super(uuid);
    }

    public static BasketId createNew() {
        return new BasketId(UUID.randomUUID());
    }

    public static BasketId fromExisting(String id) {
        return new BasketId(UUID.fromString(id));
    }
}
