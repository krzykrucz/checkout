package com.krzykrucz.checkout.application;

import com.krzykrucz.checkout.application.command.AddItemToBasketCommand;
import com.krzykrucz.checkout.application.command.AddItemToBasketCommandValidator;
import com.krzykrucz.checkout.application.command.CloseBasketCommand;
import com.krzykrucz.checkout.application.command.CloseBasketCommandValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static java.lang.String.format;

@RestController
@RequestMapping("/basket")
class BasketController {

    private static final String VALIDATION_FAILED_ERROR_MESSAGE = "Request invalid: %s";

    private final BasketApplicationService basketApplicationService;

    private final AddItemToBasketCommandValidator addItemToBasketCommandValidator;

    private final CloseBasketCommandValidator closeBasketCommandValidator;

    public BasketController(BasketApplicationService basketApplicationService, AddItemToBasketCommandValidator addItemToBasketCommandValidator, CloseBasketCommandValidator closeBasketCommandValidator) {
        this.basketApplicationService = basketApplicationService;
        this.addItemToBasketCommandValidator = addItemToBasketCommandValidator;
        this.closeBasketCommandValidator = closeBasketCommandValidator;
    }

    @PostMapping("/open")
    public OpenBasketResult openBasket() {
        return basketApplicationService.openBasket();
    }

    @PostMapping("/addItem")
    public void addItemToBaset(@Valid @RequestBody AddItemToBasketCommand addItemCommand) {
        basketApplicationService.addItemToBasket(addItemCommand);
    }

    @PostMapping("/close")
    public CloseBasketResult closeBasket(@Valid @RequestBody CloseBasketCommand closeBasketCommand) {
        return basketApplicationService.closeBasket(closeBasketCommand);
    }

    @InitBinder("closeBasketCommand")
    private void initCloseBinder(WebDataBinder binder) {
        binder.addValidators(closeBasketCommandValidator);
    }

    @InitBinder("addItemToBasketCommand")
    private void initAddBinder(WebDataBinder binder) {
        binder.addValidators(addItemToBasketCommandValidator);
    }

    @ExceptionHandler(BasketOperationException.class)
    public ResponseEntity<BasketOperationFailedResponse> handleBasketOperationException(BasketOperationException exception) {
        final BasketOperationFailedResponse response = new BasketOperationFailedResponse(exception.getErrorMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BasketOperationFailedResponse> handleException(MethodArgumentNotValidException exception) {
        final FieldError fieldError = exception.getBindingResult()
                .getFieldError();
        final String errorReason = fieldError != null ? fieldError.getDefaultMessage() : exception.getMessage();
        final String errorMessage = format(VALIDATION_FAILED_ERROR_MESSAGE, errorReason);
        final BasketOperationFailedResponse response = new BasketOperationFailedResponse(errorMessage);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
