package com.toast.oneq.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.toast.oneq.controller.ApiController;

public class LogMaker {
    private static final Logger logger = LoggerFactory.getLogger(ApiController.class);
    
    public static void info(String message){
        logger.info(getLogMassage(message));
    } 
    
    public static void warn(String message){
        logger.warn(getLogMassage(message));
    }
    
    public static void error(String message){
        logger.error(getLogMassage(message));
    }
    
    protected static String getLogMassage(String message){
        return "message=[" + message + "]";
    }
    
    
}
