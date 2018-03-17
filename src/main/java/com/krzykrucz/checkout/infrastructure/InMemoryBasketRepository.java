package com.krzykrucz.checkout.infrastructure;

import com.krzykrucz.checkout.domain.basket.Basket;
import com.krzykrucz.checkout.domain.basket.BasketId;
import com.krzykrucz.checkout.domain.basket.BasketRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
class InMemoryBasketRepository extends InMemoryRepository<BasketId, Basket> implements BasketRepository {

    @Override
    public void save(Basket basket) {
        super.store(basket.getBasketId(), basket);
    }

    @Override
    public Optional<Basket> findOne(BasketId id) {
        return super.fetchById(id);
    }

}
