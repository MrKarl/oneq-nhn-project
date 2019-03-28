package com.toast.oneq.exceptionhandler;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.toast.oneq.exception.QuestionInsertFormException;
import com.toast.oneq.exception.ServiceFailException;
import com.toast.oneq.exception.UnauthorizedUserException;
import com.toast.oneq.log.LogMaker;
import com.toast.oneq.util.ResponseHeaderUtil;
import com.toast.oneq.vo.ResponseVo;

@ControllerAdvice(basePackageClasses={com.toast.oneq.controller.ApiController.class})
public class ApiExceptionHandler {
    private Map<String,Object> header;
    
    @ExceptionHandler(UnauthorizedUserException.class)
    public @ResponseBody ResponseVo handleUnauthorizedUserException(UnauthorizedUserException e) {
        LogMaker.info(e.toString());
        header = ResponseHeaderUtil.createHeader(500, e.getMessage(), false);
        return new ResponseVo().setHeader(header);
    }
    
    @ExceptionHandler(ServiceFailException.class)
    public @ResponseBody ResponseVo handleServiceFailException(ServiceFailException e) {
        LogMaker.error(e.toString());
        header = ResponseHeaderUtil.createHeader(500, e.getErrorMessageForClient(), false);
        return new ResponseVo().setHeader(header);
    }

    @ExceptionHandler(QuestionInsertFormException.class)
    public @ResponseBody ResponseVo handleQuestionInsertFormException(Exception e) throws JSONException {
        LogMaker.info(e.toString());
        String body = e.getMessage();
        JSONObject json = new JSONObject(body);
        
        header = ResponseHeaderUtil.createHeader(406, json.toString(), false);
        
        return new ResponseVo().setHeader(header);//.setBody(json.toString());
    }
    
    @ExceptionHandler(Exception.class)
    public @ResponseBody ResponseVo handleException(Exception e) {
        LogMaker.error(e.toString());
        header = ResponseHeaderUtil.createHeader(500, "서버 에러 입니다.", false);
        return new ResponseVo().setHeader(header);
    }
}
