package com.krzykrucz.checkout.application.command;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class AddItemToBasketCommandValidator implements Validator {

    private static final String ERROR_CODE = "invalidField";

    @Override
    public boolean supports(Class<?> aClass) {
        return AddItemToBasketCommand.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        final AddItemToBasketCommand command = (AddItemToBasketCommand) o;
        if (command.getBasketId() == null) {
            errors.rejectValue("basketId", ERROR_CODE, "basket id is not present");
        }
        if (command.getProductId() == null) {
            errors.rejectValue("productId", ERROR_CODE, "product id is not present");
        }
        final Integer quantity = command.getQuantity();
        if (quantity == null) {
            errors.rejectValue("quantity", ERROR_CODE, "quantity is not present");
        }
        if (quantity != null && quantity < 0) {
            errors.rejectValue("quantity", ERROR_CODE, "quantity is negative");
        }
    }
}
