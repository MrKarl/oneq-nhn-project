package com.toast.oneq.oauth;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.spy;

import java.util.Collection;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.toast.oneq.vo.FacebookVo;
import com.toast.oneq.vo.UserVo;

@RunWith(MockitoJUnitRunner.class)
public class FacebookOauthTest {

    private static final String DIALOG_OAUTH = "https://www.facebook.com/dialog/oauth";
    private static final int FACKBOOKCODE = 2;

//    @InjectMocks
//    private FacebookOauth facebookOauth;
    private FacebookOauth facebookOauth;
    
    private RedirectAttributes redirectAttributes;
    
    private FacebookVo facebookVo;
    private UserVo userVo;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        
        redirectAttributes = new RedirectAttributes() {
            
            @Override
            public boolean containsAttribute(String attributeName) {
                // TODO Auto-generated method stub
                return false;
            }
            
            @Override
            public Map<String, Object> asMap() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public Model addAllAttributes(Map<String, ?> attributes) {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public RedirectAttributes mergeAttributes(Map<String, ?> attributes) {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public Map<String, ?> getFlashAttributes() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public RedirectAttributes addFlashAttribute(String attributeName, Object attributeValue) {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public RedirectAttributes addFlashAttribute(Object attributeValue) {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public RedirectAttributes addAttribute(String attributeName, Object attributeValue) {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public RedirectAttributes addAttribute(Object attributeValue) {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public RedirectAttributes addAllAttributes(Collection<?> attributeValues) {
                // TODO Auto-generated method stub
                return null;
            }
        };
    
        facebookOauth = spy(new FacebookOauth("scope","redirectUri","clientId","appSecret",this.DIALOG_OAUTH,"accesstoken","me","fields"));
        
        facebookVo = new FacebookVo()
                        .setId("testId")
                        .setName("testName");
    }
    
    @Test
    public void oauthFacebookTest() throws Exception {
        assertThat(facebookOauth.oauthFacebook(redirectAttributes), equalTo(this.DIALOG_OAUTH));
    }
    
    @Test
    public void getUserInfoTest() throws Exception {
        Mockito.doReturn(facebookVo).when(facebookOauth).getAccessToken(Mockito.anyString());
        Mockito.doReturn(facebookVo).when(facebookOauth).getUserIdNName(facebookVo);
        
        userVo = facebookOauth.getUserInfo(Mockito.anyString());
        
        assertThat(userVo.getOAuthId(), equalTo("testId"));
        assertThat(userVo.getUserName(), equalTo("testName"));
        assertThat(userVo.getProviderCode(), equalTo(FACKBOOKCODE));
    }
    
    @Test
    public void getFacebookCodeTest() {
        assertThat(facebookOauth.getFacebookCode(), equalTo(this.FACKBOOKCODE));
    }
    
}
