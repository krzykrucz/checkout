package com.krzykrucz.checkout.application;

import com.krzykrucz.checkout.domain.basket.BasketId;
import lombok.Getter;

@Getter
class OpenBasketResult {

    private final String basketId;

    OpenBasketResult(BasketId basketId) {
        this.basketId = basketId.toString();
    }

}
