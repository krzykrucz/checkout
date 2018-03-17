package com.krzykrucz.checkout.domain.basket;

import java.util.Optional;

public interface BasketRepository {

    void save(Basket basket);

    Optional<Basket> findOne(BasketId id);

}
