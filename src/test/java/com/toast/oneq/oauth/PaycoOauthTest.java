package com.toast.oneq.oauth;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Collection;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.toast.oneq.vo.PaycoVo;
import com.toast.oneq.vo.UserVo;

@RunWith(MockitoJUnitRunner.class)
public class PaycoOauthTest {

    private static final String CLIENT_ID = "ALPNMFRpysaQv_wC8yze";
    private static final String RESPONSE_TYPE = "code";
    private static final String APP_SECRET = "zivjYjUoVD";
    private static final String SERVICE_PROVIDER_CODE = "PAY";
    private static final String REDIRECT_URI = "http://oneq.nhnent.com/oauth/payco/callback";
    private static final String STATE = "randomstring";
    private static final String REQUEST_CODE = "https://alpha-id.payco.com/oauth2.0/authorize";
    private static final String GRANT_TYPE = "authorization_code";
    private static final String ACCESS_TOKEN = "https://alpha-id.payco.com/oauth2.0/token";
    private static final String GET_PROFILE = "http://tcc1-id-bo.payco.com:10003/neid_bo/oauth/getProfileBasicByToken";
    private static final int PAYCOCODE = 0;
    
    @Mock
    private RestTemplate restTemplate;
    
    private PaycoOauth paycoOauth;
    
    private RedirectAttributes redirectAttributes;
    
    private PaycoVo paycoVo;
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
    
        paycoOauth = Mockito.spy(new PaycoOauth("ci","rt","as","sp","ru",this.REQUEST_CODE,"gt","at","gp",this.PAYCOCODE));
        
        paycoVo = new PaycoVo();
        paycoVo.setIdNo("testId");
    }
    
    @Test
    public void oauthPaycoTest() throws Exception {
        assertThat(paycoOauth.oauthPayco(redirectAttributes), equalTo(this.REQUEST_CODE));
    }
    
    @Test
    public void getUserInfoTest() throws Exception {
        Mockito.doReturn(paycoVo).when(paycoOauth).getAccessToken(Mockito.anyString());
        Mockito.doReturn(paycoVo).when(paycoOauth).getUserId(Mockito.anyString());
        
        userVo = paycoOauth.getUserInfo(Mockito.anyString(), Mockito.anyString());
        
        assertThat(userVo.getOAuthId(), equalTo("testId"));
        assertThat(userVo.getProviderCode(), equalTo(this.PAYCOCODE));
    }
    
    @Test
    public void getPaycoCodeTest() throws Exception {
        assertThat(paycoOauth.getPaycoCode(), equalTo(this.PAYCOCODE));
    }
    
}
