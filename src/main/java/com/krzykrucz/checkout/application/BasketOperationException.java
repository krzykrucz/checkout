package com.krzykrucz.checkout.application;

import com.krzykrucz.checkout.domain.product.ProductId;
import com.krzykrucz.checkout.domain.product.ProductInfo;

class BasketOperationException extends RuntimeException {


    private BasketOperationException(String errorMessage) {
        super(errorMessage);
    }

    static BasketOperationException basketNotFound() {
        return new BasketOperationException("Basket not found");
    }

    static BasketOperationException addingNonExistentProduct(ProductId productId) {
        return new BasketOperationException("Cannot add product with id " + productId + " because it doesn't exist");
    }

    static BasketOperationException nonUpToDateProduct(ProductInfo product) {
        return new BasketOperationException("Basket contains non-up-to-date product: " + product);
    }

    String getErrorMessage() {
        return super.getMessage();
    }
}
