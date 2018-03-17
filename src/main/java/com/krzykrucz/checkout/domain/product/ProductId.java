package com.krzykrucz.checkout.domain.product;

import com.krzykrucz.checkout.common.AggregateRootId;

import java.util.UUID;

public class ProductId extends AggregateRootId {
    public ProductId(UUID uuid) {
        super(uuid);
    }

    public static ProductId createNew() {
        return new ProductId(UUID.randomUUID());
    }

    public static ProductId fromExisting(String id) {
        return new ProductId(UUID.fromString(id));
    }
}
