package com.pnk.patient_management.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
public class UnsupportedMediaType extends RuntimeException {

    public UnsupportedMediaType(String message) {

        super(message);

    }

}
