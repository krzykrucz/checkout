package com.krzykrucz.checkout.application.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.krzykrucz.checkout.domain.product.ProductId;

import java.io.IOException;

public class ProductIdDeserializer extends StdDeserializer<ProductId> {

    private static final long serialVersionUID = 1L;

    public ProductIdDeserializer() {
        super(ProductId.class);
    }

    @Override
    public ProductId deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        final JsonNode idTree = jp.readValueAsTree();
        final String id = idTree.textValue();
        return ProductId.fromExisting(id);
    }
}