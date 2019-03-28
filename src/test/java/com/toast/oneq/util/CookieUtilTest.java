package com.toast.oneq.util;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletResponse;

@RunWith(MockitoJUnitRunner.class)
public class CookieUtilTest {

    private MockHttpServletResponse response;
    private String userId;
    
    @Before
    public void setUp() {
        response = new MockHttpServletResponse();
        userId = "testId";
    }
    
    @Test
    public void setUserIdToResponseTest() {
        CookieUtil.setUuidToResponse(response, userId);
    }
    
    @Test
    public void unsetUuidToResponseTest(){
        CookieUtil.unsetUuidToResponse(response, userId);
    }
    
    @Test
    public void setSessionIdToResponseTest(){
        CookieUtil.setSessionIdToResponse(response, userId);
    }
    
    @Test
    public void unsetSessionIdToResponseTest(){
        CookieUtil.unsetSessionIdToResponse(response, userId);
    }
}
