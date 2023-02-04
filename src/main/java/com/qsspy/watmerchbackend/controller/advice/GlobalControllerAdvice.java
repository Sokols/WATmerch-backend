package com.qsspy.watmerchbackend.controller.advice;

import com.qsspy.watmerchbackend.exception.HttpErrorResponseModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<HttpErrorResponseModel> handleException(Exception exc) {


        HttpErrorResponseModel model = new HttpErrorResponseModel();

        model.setStatusCode(HttpStatus.NOT_ACCEPTABLE.value());
        model.setMessage(HttpStatus.NOT_ACCEPTABLE.name());
        model.setDetails(exc.getMessage());

        return new ResponseEntity<>(model, HttpStatus.NOT_ACCEPTABLE);
    }
}
