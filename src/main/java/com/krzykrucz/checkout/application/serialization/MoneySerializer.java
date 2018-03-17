package com.krzykrucz.checkout.application.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.joda.money.Money;

import java.io.IOException;

public class MoneySerializer extends JsonSerializer<Money> {

    @Override
    public void serialize(Money value, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException, JsonProcessingException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("stringValue", value.getAmount().toString());
        jsonGenerator.writeStringField("currencySymbol", value.getCurrencyUnit().getCode());
        jsonGenerator.writeStringField("formattedMoney", getFormattedValue(value));
        jsonGenerator.writeEndObject();
    }

    private String getFormattedValue(Money money) {
        return money.getCurrencyUnit().getSymbol() + money.getAmount().toPlainString();
    }
}