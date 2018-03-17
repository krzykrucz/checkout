package com.krzykrucz.checkout.application;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.krzykrucz.checkout.application.serialization.MoneySerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.joda.money.Money;

@AllArgsConstructor
@Getter
class CloseBasketResult {

    @JsonSerialize(using = MoneySerializer.class)
    private final Money totalPrice;

}
