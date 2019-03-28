package com.toast.oneq.oauth;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.notNull;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.matchers.NotNull;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.reflect.internal.WhiteboxImpl;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.toast.oneq.redis.TwitterSessionDao;
import com.toast.oneq.vo.UserVo;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

@RunWith(MockitoJUnitRunner.class)
@PrepareForTest( { Twitter.class } )
public class TwitterOauthTest {
    @InjectMocks
    private TwitterOauth twitterOauth = new TwitterOauth(this.CONSUMERKEY,this.CONSUMERSECRET,1,this.REDIRECURL);
    @Mock
    private Twitter twitter;
    @Mock
    private User twitterUser;
    @Mock
    private TwitterSessionDao twitterSessionDao;
    
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private RequestToken requestToken;
    
    private static final String TWITTERAuthURL = "https://api.twitter.com/oauth/authenticate?oauth_token";;
    private static final String CONSUMERKEY = "inCQOqXJhfroZjieDgdo0CToD";
    private static final String CONSUMERSECRET = "QtCmxPktcGkYwOk9njAYHopX1xWSzohMfBut4jxMpbRTsOubAx";
    private static final String REDIRECURL = "http://alpha-oneq.nhnent.com/oauth/twitter/callback";

    
    @Before
    public void setup() throws TwitterException{
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        
        Twitter twitter = new TwitterFactory().getInstance();
        twitter.setOAuthConsumer(CONSUMERKEY, CONSUMERSECRET);

        requestToken = twitter.getOAuthRequestToken(REDIRECURL);
    }
    
    @Test
    public void oauthTwitterTest() throws Exception{
        String authenticationUrl = twitterOauth.oauthTwitter(response);
        String authenticationUrlList[] = authenticationUrl.split("=");
        assertThat(authenticationUrlList[0], equalTo(TWITTERAuthURL));
    }
    
    @Test
    public void getUserInfoTest_oauthToken_값없을때() throws Exception{
        Mockito.when(twitterSessionDao.getToken(anyString())).thenReturn("token");
        Mockito.when(twitterSessionDao.getTokenSecret(anyString())).thenReturn("tokenSecret");
        
        assertThat(twitterOauth.getUserInfo(request, response).getUserId(), equalTo(0));
    }

    @Test
    public void getUserInfoTest_oauthToken이_유효하지않을때() throws Exception{
        Mockito.when(twitterSessionDao.getToken(anyString())).thenReturn("token");
        Mockito.when(twitterSessionDao.getTokenSecret(anyString())).thenReturn("tokenSecret");
        assertThat(twitterOauth.getUserInfo(request, response).getUserId(), equalTo(0));
    }
    
    @Test
    public void getUserInfoFromTwitterTest_정상() throws Exception{
        AccessToken accessToken = new AccessToken("Y6oKMQAAAAAAkDb0AAABUuRSUSU", "7l5YjEw6NsvyoKQpGn7IFixiGMNcK0Kc");
        UserVo user = new UserVo().setUserId(0);
        
        Mockito.when(twitter.getOAuthAccessToken((RequestToken)notNull(), anyString())).thenReturn(accessToken);
        Mockito.when(twitter.showUser(-1)).thenReturn(twitterUser);
        
        assertThat(twitterOauth.getUserInfoFromTwitter(twitter,requestToken,"7l5YjEw6NsvyoKQpGn7IFixiGMNcK0Kc").getUserId(), equalTo(user.getUserId()));
    }
    
    @Test(expected=NullPointerException.class)
    public void getUserInfoFromTwitterTest_verifier가_null일경우() throws Exception{
        twitterOauth.getUserInfoFromTwitter(twitter,null,null).getUserId();    
    }
    
    @Test
    public void getSessionIdFromCookieTest(){
        Cookie cookie = new Cookie("sessionId", "200");
        request.setCookies(cookie);
        assertThat(twitterOauth.getSessionIdFromCookie(request) ,equalTo("200"));
    }
    
    @Test
    public void getTwitterCodeTest(){
        int TWITTERCODE = 1;
        assertThat(twitterOauth.getTwitterCode(), equalTo(TWITTERCODE));
    }
}