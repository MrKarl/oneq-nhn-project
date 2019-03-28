package com.toast.oneq.exception;

import org.springframework.web.util.NestedServletException;

public class UnauthorizedUserException extends NestedServletException{
    private static final long serialVersionUID = 1L;

    public UnauthorizedUserException(){
        super("유효하지 않은 유저입니다.");
    }
}
