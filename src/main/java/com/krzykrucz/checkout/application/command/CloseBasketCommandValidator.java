package com.krzykrucz.checkout.application.command;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class CloseBasketCommandValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return CloseBasketCommand.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        final CloseBasketCommand command = (CloseBasketCommand) o;
        if (command.getBasketId() == null) {
            errors.rejectValue("basketId", "invalidField", "basket id is not present");
        }
    }
}
