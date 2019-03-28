package com.toast.oneq.exception;

public class ObjectStorageException extends Exception{
    private static final long serialVersionUID = 1L;

    public ObjectStorageException(String message){
        super(message);        
    }
    
    public ObjectStorageException(Throwable message){
        super(message);
    }
}

