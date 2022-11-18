package com.nhnacademy.springmvc.controller;

import com.nhnacademy.springmvc.exception.ValidationFailedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.rmi.StubNotFoundException;

@Slf4j
@ControllerAdvice
public class WebControllerAdvice {
    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, Model model) {
        log.error("", ex);

        model.addAttribute("exception", ex);
        return "thymeleaf/error";
    }
}
