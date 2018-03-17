package com.krzykrucz.checkout.infrastructure;

import com.krzykrucz.checkout.domain.Price;
import com.krzykrucz.checkout.domain.product.Product;
import com.krzykrucz.checkout.domain.product.ProductRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
@Profile("!test")
class ProductInitializer implements ApplicationRunner {

    private final ProductRepository productRepository;

    public ProductInitializer(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        final Product product1 = Product.newUndiscountableProduct("product1", Price.newDollarPrice(5));
        final Product product2 = Product.newUndiscountableProduct("product2", Price.newDollarPrice(15));
        final Product product3 = Product.newProductWithQuantityDiscount()
                .productName("product3")
                .productPrice(Price.newDollarPrice(15))
                .discountableQuantity(2)
                .discountedPrice(Price.newDollarPrice(20))
                .build();
        final Product product4 = Product.newProductWithQuantityDiscount()
                .productName("product4")
                .productPrice(Price.newDollarPrice(25))
                .discountableQuantity(1)
                .discountedPrice(Price.newDollarPrice(20))
                .build();
        final Product product5 = Product.newProductWithQuantityDiscount()
                .productName("product5")
                .productPrice(Price.newDollarPrice(35))
                .discountableQuantity(4)
                .discountedPrice(Price.newDollarPrice(100))
                .build();
        Stream.of(product1, product2, product3, product4, product5)
                .forEach(productRepository::save);
    }

}
