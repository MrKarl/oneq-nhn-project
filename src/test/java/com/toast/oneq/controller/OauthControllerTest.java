package com.toast.oneq.controller;

import static org.mockito.Matchers.notNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.toast.oneq.oauth.FacebookOauth;
import com.toast.oneq.oauth.PaycoOauth;
import com.toast.oneq.oauth.TwitterOauth;
import com.toast.oneq.redis.UserSessionDao;
import com.toast.oneq.service.UserService;
import com.toast.oneq.vo.UserVo;

@RunWith(MockitoJUnitRunner.class)
public class OauthControllerTest {
    
    @Mock
    private TwitterOauth twitterOauth;
    
    @Mock
    private PaycoOauth paycoOauth;
    
    @Mock
    private FacebookOauth facebookOauth;
    
    @Mock
    private UserService userService;
    
    @Mock
    private UserSessionDao userSessionDao;
    
    @InjectMocks
    private OauthController oauthController;
    
    private MockMvc mockMvc;
    
    private MockHttpServletResponse response;
    private RedirectAttributes redirectAttributes;
    
    private UserVo facebookUserVo;
    private UserVo twitterUserVo;
    private UserVo paycoUserVo;
    private String codeTest;
    private String stateTest;
    
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        
        response = new MockHttpServletResponse();
        redirectAttributes = Mockito.mock(RedirectAttributes.class);
        
        twitterUserVo = new UserVo().setOAuthId("testTwitterOauthId")
                                    .setUserName("TwitterUser")
                                    .setProviderCode(twitterOauth.getTwitterCode());
        
        facebookUserVo = new UserVo().setOAuthId("testFacebookOauthId")
                                     .setUserName("FacebookUser")
                                     .setProviderCode(facebookOauth.getFacebookCode());
        
        paycoUserVo = new UserVo().setOAuthId("testPaycoOauthId")
                                  .setProviderCode(paycoOauth.getPaycoCode());
        
        codeTest = "test code";
        stateTest = "test state";
        
        mockMvc = MockMvcBuilders.standaloneSetup(oauthController).build();
    }
    
    @Test
    public void redirectOauthTwitterUrlTest() throws Exception {
        Mockito.when(twitterOauth.oauthTwitter(response)).thenReturn("/oauth/twitter/callback");
        mockMvc.perform(get("/oauth/twitter/")).andExpect(status().isFound());
    }
    
    @Test
    public void callbackTwitterOauthTest() throws Exception {
        Mockito.when(twitterOauth.getUserInfo((HttpServletRequest)notNull(),(HttpServletResponse)notNull())).thenReturn(twitterUserVo);
        Mockito.when(userService.getValidUserId(twitterUserVo)).thenReturn(100);
        
        mockMvc.perform(get("/oauth/twitter/callback")).andExpect(status().isOk());
    }
    
    @Test
    public void redirectOauthPaycoUrlTest() throws Exception {
        Mockito.when(paycoOauth.oauthPayco(redirectAttributes)).thenReturn("/oauth/payco/callback");
        mockMvc.perform(get("/oauth/payco/")).andExpect(status().isFound());
    }
    
    @Test
    public void callbackPaycoOauthTest() throws Exception {
        Mockito.when(paycoOauth.getUserInfo(codeTest, stateTest)).thenReturn(paycoUserVo);
        Mockito.when(userService.getValidUserId(paycoUserVo)).thenReturn(100);
        mockMvc.perform(
                    get("/oauth/payco/callback")
                    .param("code", codeTest)
                    .param("state", stateTest)
                ).andExpect(status().isOk());
    }
    
    @Test
    public void redirectOauthFacebookUrlTest() throws Exception {
        Mockito.when(paycoOauth.oauthPayco(redirectAttributes)).thenReturn("/oauth/facebook/callback");
        mockMvc.perform(get("/oauth/facebook/")).andExpect(status().isFound()); // 302 redirect
    }
    
    @Test
    public void callbackFacebookOauthTest() throws Exception {
        Mockito.when(facebookOauth.getUserInfo(codeTest)).thenReturn(facebookUserVo);
        Mockito.when(userService.getValidUserId(facebookUserVo)).thenReturn(100);
        mockMvc.perform(
                    get("/oauth/facebook/callback")
                    .param("code", codeTest)
                ).andExpect(status().isOk());
    }
}
