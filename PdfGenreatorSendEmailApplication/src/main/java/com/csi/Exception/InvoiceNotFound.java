package com.csi.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class InvoiceNotFound extends Exception {
    public InvoiceNotFound(String message) {
        super(message);
    }
}