package com.toast.oneq.vo;

import java.util.Map;

public class ResponseVo {
    private Map<String,Object> header;
    private Object body;
    
    public Map<String,Object> getHeader() {
        return header;
    }
    public Object getBody() {
        return body;
    }
    public ResponseVo setHeader(Map<String, Object> header) {
        this.header = header;
        return this;
    }
    public ResponseVo setBody(Object body){
        this.body = body;
        return this;
    }
}
