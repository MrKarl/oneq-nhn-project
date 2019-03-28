package com.toast.oneq.exception;

import org.springframework.web.util.NestedServletException;

public class ServiceFailException extends NestedServletException{
    private static final long serialVersionUID = 1L;
    private String errorMessageForClient;
    public ServiceFailException(String errorMessage){
        super(errorMessage);
        this.errorMessageForClient = errorMessage;
    }
    
    public ServiceFailException(String errorMessage, String errorMessageForClient){
        super(errorMessage);
        this.errorMessageForClient = errorMessageForClient;
    }
    
    public String getErrorMessageForClient(){
        return this.errorMessageForClient;
    }
}

