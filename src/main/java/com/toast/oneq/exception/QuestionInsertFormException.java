package com.toast.oneq.exception;

import org.springframework.web.util.NestedServletException;

public class QuestionInsertFormException extends NestedServletException{
    private static final long serialVersionUID = 1L;

    public QuestionInsertFormException(String errorMessage){
        super(errorMessage);
    }
}