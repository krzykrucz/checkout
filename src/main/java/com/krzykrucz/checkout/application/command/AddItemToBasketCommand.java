package com.krzykrucz.checkout.application.command;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.krzykrucz.checkout.application.serialization.BasketIdDeserializer;
import com.krzykrucz.checkout.application.serialization.ProductIdDeserializer;
import com.krzykrucz.checkout.domain.basket.BasketId;
import com.krzykrucz.checkout.domain.product.ProductId;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AddItemToBasketCommand {

    @JsonDeserialize(using = BasketIdDeserializer.class)
    private BasketId basketId;

    @JsonDeserialize(using = ProductIdDeserializer.class)
    private ProductId productId;

    private Integer quantity;

}
