package com.toast.oneq.util;

import java.util.HashMap;
import java.util.Map;

public class ResponseHeaderUtil {
    public static final Map<String, Object> RESPONSE_SUCCESS_HEADER = createHeader(200, "success", true);
    
    public static Map<String, Object> createHeader(int resultCode, String resultMessage, boolean isSuccessful) {
        Map<String, Object> header = new HashMap<String, Object>();
        header.put("resultCode", resultCode);
        header.put("resultMessage", resultMessage);
        header.put("isSuccessful", isSuccessful);
        return header;
    }
    
}
