package com.krzykrucz.checkout.application;

import com.krzykrucz.checkout.application.command.AddItemToBasketCommand;
import com.krzykrucz.checkout.application.command.CloseBasketCommand;
import com.krzykrucz.checkout.domain.Price;
import com.krzykrucz.checkout.domain.basket.Basket;
import com.krzykrucz.checkout.domain.basket.BasketItem;
import com.krzykrucz.checkout.domain.basket.BasketRepository;
import com.krzykrucz.checkout.domain.product.Product;
import com.krzykrucz.checkout.domain.product.ProductId;
import com.krzykrucz.checkout.domain.product.ProductInfo;
import com.krzykrucz.checkout.domain.product.ProductRepository;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkState;

@Service
public class BasketApplicationService {

    private final BasketRepository basketRepository;

    private final ProductRepository productRepository;

    public BasketApplicationService(BasketRepository basketRepository, ProductRepository productRepository) {
        this.basketRepository = basketRepository;
        this.productRepository = productRepository;
    }

    public OpenBasketResult openBasket() {
        final Basket newBasket = new Basket();
        basketRepository.save(newBasket);
        return new OpenBasketResult(newBasket.getBasketId());
    }


    public void addItemToBasket(AddItemToBasketCommand command) {
        final ProductId productId = command.getProductId();
        final Basket basket = basketRepository.findOne(command.getBasketId())
                .orElseThrow(BasketOperationException::basketNotFound);
        final Product productToAdd = productRepository.findOne(productId)
                .orElseThrow(() -> BasketOperationException.addingNonExistentProduct(productId));
        basket.addItem(productToAdd, command.getQuantity());
    }

    public CloseBasketResult closeBasket(CloseBasketCommand command) {
        final Basket basketToClose = basketRepository.findOne(command.getBasketId())
                .orElseThrow(BasketOperationException::basketNotFound);
        checkBasketValidity(basketToClose);
        final Price total = basketToClose.close();
        basketRepository.save(basketToClose);
        return new CloseBasketResult(total.getValue());
    }

    private void checkBasketValidity(Basket basketToClose) {
        basketToClose.getItems()
                .stream()
                .map(BasketItem::getProductInfo)
                .filter(this::productNotUpToDate)
                .findAny()
                .ifPresent(product -> {
                    throw BasketOperationException.nonUpToDateProduct(product);
                });
    }

    private boolean productNotUpToDate(ProductInfo productInfo) {
        final Optional<Product> product = productRepository.findOne(productInfo.getProductId());
        checkState(product.isPresent());
        final ProductInfo newestProductInfo = product.get().generateInfo(Clock.systemDefaultZone());
        return !newestProductInfo.equals(productInfo);
    }
}
