package com.krzykrucz.checkout.domain.product;

import com.krzykrucz.checkout.domain.Price;
import com.krzykrucz.checkout.domain.discount.DiscountPolicy;
import com.krzykrucz.checkout.domain.discount.NoneDiscountPolicy;
import com.krzykrucz.checkout.domain.discount.QuantityDiscountPolicy;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Getter
@EqualsAndHashCode(of = "productId")
public class Product {

    private final ProductId productId;

    private String name;

    private Price price;

    private DiscountPolicy availableDiscount;

    private Product(ProductId productId, String name, Price price, DiscountPolicy availableDiscount) {
        final Optional<ProductId> discountPolicyApplicableProduct = availableDiscount.applicableProduct();
        checkArgument(isNotBlank(name));
        checkArgument(!discountPolicyApplicableProduct.isPresent() || discountPolicyApplicableProduct.get().equals(productId));

        this.productId = checkNotNull(productId);
        this.price = checkNotNull(price);
        this.availableDiscount = checkNotNull(availableDiscount);
        this.name = checkNotNull(name);
    }

    public static Product newUndiscountableProduct(String name, Price price) {
        return new Product(ProductId.createNew(), name, price, NoneDiscountPolicy.instance());
    }

    public static ProductWithQuantityDiscountBuilder newProductWithQuantityDiscount() {
        return new ProductWithQuantityDiscountBuilder();
    }

    public void updatePrice(Price newPrice) {
        this.price = newPrice;
    }

    public ProductInfo generateInfo(Clock clock) {
        return new ProductInfo(productId, name, price, LocalDateTime.now(clock), availableDiscount);
    }

    public static class ProductWithQuantityDiscountBuilder {
        private int discountableQuantity;
        private Price discountedPrice;
        private String productName;
        private Price productPrice;

        private ProductWithQuantityDiscountBuilder() {
        }

        public ProductWithQuantityDiscountBuilder discountableQuantity(int discountableQuantity) {
            this.discountableQuantity = discountableQuantity;
            return this;
        }

        public ProductWithQuantityDiscountBuilder discountedPrice(Price discountedPrice) {
            this.discountedPrice = discountedPrice;
            return this;
        }

        public ProductWithQuantityDiscountBuilder productName(String productName) {
            this.productName = productName;
            return this;
        }

        public ProductWithQuantityDiscountBuilder productPrice(Price productPrice) {
            this.productPrice = productPrice;
            return this;
        }

        public Product build() {
            final ProductId productId = ProductId.createNew();
            final QuantityDiscountPolicy quantityDiscountPolicy =
                    new QuantityDiscountPolicy(productId, discountableQuantity, discountedPrice);
            return new Product(productId, productName, productPrice, quantityDiscountPolicy);
        }

    }

}
