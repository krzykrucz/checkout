package com.krzykrucz.checkout.application;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
class BasketOperationFailedResponse {

    private final String errorMessage;

}
