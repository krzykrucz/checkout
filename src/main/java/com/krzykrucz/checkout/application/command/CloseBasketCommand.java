package com.krzykrucz.checkout.application.command;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.krzykrucz.checkout.application.serialization.BasketIdDeserializer;
import com.krzykrucz.checkout.domain.basket.BasketId;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CloseBasketCommand {

    @JsonDeserialize(using = BasketIdDeserializer.class)
    private BasketId basketId;

}
