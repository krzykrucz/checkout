package com.krzykrucz.checkout.common;

import lombok.EqualsAndHashCode;

import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

@EqualsAndHashCode
public abstract class AggregateRootId {

    private final UUID uuid;

    protected AggregateRootId(UUID uuid) {
        this.uuid = checkNotNull(uuid);
    }

    @Override
    public String toString() {
        return uuid.toString();
    }
}
