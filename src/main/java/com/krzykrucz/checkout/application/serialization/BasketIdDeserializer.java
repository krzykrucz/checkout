package com.krzykrucz.checkout.application.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.krzykrucz.checkout.domain.basket.BasketId;

import java.io.IOException;

public class BasketIdDeserializer extends StdDeserializer<BasketId> {

    private static final long serialVersionUID = 1L;

    public BasketIdDeserializer() {
        super(BasketId.class);
    }

    @Override
    public BasketId deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        final JsonNode idTree = jp.readValueAsTree();
        final String id = idTree.textValue();
        return BasketId.fromExisting(id);
    }
}