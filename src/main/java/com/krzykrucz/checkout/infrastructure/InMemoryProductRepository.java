package com.krzykrucz.checkout.infrastructure;

import com.krzykrucz.checkout.domain.product.Product;
import com.krzykrucz.checkout.domain.product.ProductId;
import com.krzykrucz.checkout.domain.product.ProductRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
class InMemoryProductRepository extends InMemoryRepository<ProductId, Product> implements ProductRepository {

    @Override
    public void save(Product product) {
        super.store(product.getProductId(), product);
    }

    @Override
    public Optional<Product> findOne(ProductId productId) {
        return super.fetchById(productId);
    }
}
