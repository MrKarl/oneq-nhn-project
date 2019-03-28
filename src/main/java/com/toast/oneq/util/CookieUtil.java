package com.toast.oneq.util;

import javax.servlet.http.HttpServletResponse;
import org.springframework.web.util.CookieGenerator;

public class CookieUtil {
    static final int ONEDAY = 60 * 60 * 24;
    public static void setUuidToResponse(HttpServletResponse response, String uuid){
        CookieGenerator cookie = new CookieGenerator();

        cookie.setCookieName("uuid");
        cookie.setCookiePath("/");
        cookie.setCookieMaxAge(ONEDAY);
        cookie.addCookie(response, uuid);
    }
    
    public static void unsetUuidToResponse(HttpServletResponse response, String uuid){
        CookieGenerator cookie = new CookieGenerator();
        
        cookie.setCookieName("uuid");
        cookie.setCookiePath("/");
        cookie.setCookieMaxAge(0);
        cookie.addCookie(response, uuid);
    }
    
    public static void setSessionIdToResponse(HttpServletResponse response, String sessionId){
        CookieGenerator cookie = new CookieGenerator();

        cookie.setCookieName("sessionId");
        cookie.setCookiePath("/");
        cookie.setCookieMaxAge(ONEDAY);
        cookie.addCookie(response, sessionId);
    }
    
    public static void unsetSessionIdToResponse(HttpServletResponse response, String sessionId){
        CookieGenerator cookie = new CookieGenerator();
        
        cookie.setCookieName("sessionId");
        cookie.setCookiePath("/");
        cookie.setCookieMaxAge(0);
        cookie.addCookie(response, sessionId);
    }
}