package com.krzykrucz.checkout.domain.owner;

import com.krzykrucz.checkout.common.AggregateRootId;

import java.util.UUID;

public class OwnerId extends AggregateRootId {
    protected OwnerId(UUID uuid) {
        super(uuid);
    }
}
