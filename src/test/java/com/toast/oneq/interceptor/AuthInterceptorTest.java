package com.toast.oneq.interceptor;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

import javax.servlet.http.Cookie;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.toast.oneq.exception.UnauthorizedUserException;
import com.toast.oneq.redis.UserSessionDao;

@RunWith(MockitoJUnitRunner.class)
@ContextHierarchy ({
    @ContextConfiguration("/spring/application-context.xml"),
    @ContextConfiguration("/spring/servlet-context.xml")
})
public class AuthInterceptorTest extends HandlerInterceptorAdapter{
  
    @Mock
    private UserSessionDao userSessionDao;
    @InjectMocks
    private OauthInterceptor authInterceptor;
    
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private Object handler;
    private final String cookieIndexName = "uuid";
    private final String validTestUuid = "testUuid";
    
    @Before
    public void setup(){
        authInterceptor = Mockito.spy(new OauthInterceptor());
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        handler = new Object();
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void preHandleTest() throws Exception{
        Mockito.when(authInterceptor.getValidUserIdFromRedis(request,response)).thenReturn(1);
        request.setAttribute("userId", 1);
        assertThat(authInterceptor.preHandle(request, response, handler),equalTo(super.preHandle(request, response, handler)));
    }
    
    @Test
    public void getValidUserIdFromRedisTest_uuid가_없을때() throws UnauthorizedUserException{
        assertThat(authInterceptor.getValidUserIdFromRedis(request,response),equalTo(0));
    }
    
    @Test
    public void getValidUserIdFromRedisTest_uuid가_유효할때() throws UnauthorizedUserException{
        Mockito.when(authInterceptor.getUUidFromCookie(request)).thenReturn(validTestUuid);
        Mockito.when(userSessionDao.isValidUuid(validTestUuid)).thenReturn(true);
        Mockito.when(userSessionDao.getUserId(validTestUuid)).thenReturn(1);
        assertThat(authInterceptor.getValidUserIdFromRedis(request,response),equalTo(1));
    }
    
    @Test(expected = UnauthorizedUserException.class)
    public void getValidUserIdFromRedisTest_uuid가_유효하지않을때() throws UnauthorizedUserException{
        Mockito.when(authInterceptor.getUUidFromCookie(request)).thenReturn(validTestUuid);
        Mockito.when(userSessionDao.isValidUuid(validTestUuid)).thenReturn(false);
        assertThat(authInterceptor.getValidUserIdFromRedis(request,response),equalTo(Mockito.anyInt()));
    }
    
    @Test
    public void getUUidFromCookieTest() throws UnauthorizedUserException{
        Cookie cookie = new Cookie(cookieIndexName, "1");
        request.setCookies(cookie);
        assertThat(authInterceptor.getUUidFromCookie(request),equalTo("1"));
    }
}
