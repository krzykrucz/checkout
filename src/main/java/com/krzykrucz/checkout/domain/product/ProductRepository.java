package com.krzykrucz.checkout.domain.product;

import java.util.Optional;

public interface ProductRepository {

    void save(Product product);

    Optional<Product> findOne(ProductId productId);
}
