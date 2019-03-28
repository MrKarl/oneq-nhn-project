package com.toast.oneq.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.WebUtils;

import com.toast.oneq.exception.UnauthorizedUserException;
import com.toast.oneq.log.LogMaker;
import com.toast.oneq.redis.UserSessionDao;
import com.toast.oneq.util.CookieUtil;

public class OauthInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private UserSessionDao userSessionDao;
    
    private final String cookieIndexName = "uuid";
    private final String UNVALID_USER = "unvalid user access";
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        int userId = getValidUserIdFromRedis(request, response);
        request.setAttribute("userId", userId);
        return super.preHandle(request, response, handler);
    }
    
    protected int getValidUserIdFromRedis(HttpServletRequest request, HttpServletResponse response) throws UnauthorizedUserException{
        String uuid = getUUidFromCookie(request);
        if(uuid == null){
            return 0;
        } else if(userSessionDao.isValidUuid(uuid)){
            return userSessionDao.getUserId(uuid);
        } else {
            CookieUtil.unsetUuidToResponse(response, uuid);
            LogMaker.info(UNVALID_USER);
            throw new UnauthorizedUserException();
        }
    }
    
    protected String getUUidFromCookie(HttpServletRequest request){
        Cookie cookie =  WebUtils.getCookie(request, cookieIndexName);
        if(cookie == null){
            return null;
        }
        return cookie.getValue();
    }
}
