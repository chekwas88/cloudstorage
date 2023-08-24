package com.udacity.jwdnd.course1.cloudstorage;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.udacity.jwdnd.course1.cloudstorage.exception.EntityNotFoundException;
import com.udacity.jwdnd.course1.cloudstorage.exception.ErrorException;

@ControllerAdvice
public class ApplicationExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public String HandleNotFoundException(EntityNotFoundException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(ErrorException.class)
    public String HandleOperationError(ErrorException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "error";
    }

}
