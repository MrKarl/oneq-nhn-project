package com.toast.oneq.oauth;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.util.WebUtils;

import com.toast.oneq.redis.TwitterSessionDao;
import com.toast.oneq.util.CookieUtil;
import com.toast.oneq.util.UuidUtil;
import com.toast.oneq.vo.UserVo;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

public class TwitterOauth {
    @Autowired
    TwitterSessionDao twitterSessionDao;
    
    private String CONSUMERKEY;
    private String CONSUMERSECRET;
    private int TWITTERCODE;
    private String REDIRECTURI;
    
    private final String cookieIndexName = "sessionId";
    
    public TwitterOauth(String consumerKey, String consumerSecret, int twitterCode, String redirectUri){
        this.CONSUMERKEY = consumerKey;
        this.CONSUMERSECRET = consumerSecret;
        this.TWITTERCODE = twitterCode;
        this.REDIRECTURI = redirectUri;
    }
    
    public String oauthTwitter(HttpServletResponse response) throws TwitterException {
        Twitter twitter = initTwitter();
        RequestToken requestToken = twitter.getOAuthRequestToken(REDIRECTURI);
        // Redis requestToken put하고 key return, cookie에 redis key 저장.
        String sessionId = assignRequestTokenToRedis(requestToken.getToken(), requestToken.getTokenSecret());
        CookieUtil.setSessionIdToResponse(response,sessionId);
        
        return requestToken.getAuthenticationURL();
    }

    public UserVo getUserInfo(HttpServletRequest request, HttpServletResponse response) throws TwitterException {
        Twitter twitter = initTwitter();
        // cookie에서 redis key 가져와서, Redis에서 requestToken 가져온다.
        String sessionId = getSessionIdFromCookie(request);
        RequestToken requestToken = getRequestTokenFromRedis(request, sessionId);
        
        String oauthToken = request.getParameter("oauth_token");;
        String verifier = request.getParameter("oauth_verifier");

        if(!requestToken.getToken().equals(oauthToken)){
            return new UserVo();
        }
        
        twitterSessionDao.deleteSessionId(sessionId);
        CookieUtil.unsetSessionIdToResponse(response, sessionId);
        return getUserInfoFromTwitter(twitter, requestToken, verifier);
    }
    
    protected UserVo getUserInfoFromTwitter(Twitter twitter, RequestToken requestToken, String verifier) throws TwitterException{
        AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, verifier);
        String oauthId = Long.toString(twitter.showUser(accessToken.getUserId()).getId());
        String userName = twitter.showUser(accessToken.getUserId()).getName();
        
        return new UserVo().setOAuthId(oauthId)
                           .setUserName(userName)
                           .setProviderCode(TWITTERCODE);
    }
    
    protected Twitter initTwitter(){
        Twitter twitter = new TwitterFactory().getInstance();
        twitter.setOAuthConsumer(CONSUMERKEY, CONSUMERSECRET);
        return twitter;
    }
    
    protected RequestToken getRequestTokenFromRedis(HttpServletRequest request, String sessionId){
        String token = twitterSessionDao.getToken(sessionId);
        String tokenSecret = twitterSessionDao.getTokenSecret(sessionId);

        return new RequestToken(token, tokenSecret);
    }
    
    protected Twitter makeTwitter(){
        Twitter twitter = new TwitterFactory().getInstance();
        twitter.setOAuthConsumer(CONSUMERKEY, CONSUMERSECRET);
        return twitter;
    }
    
    protected String assignRequestTokenToRedis(String token, String tokenSecret) {
        String sessionId = UuidUtil.generatedUuid();
        twitterSessionDao.appendRequestToken(sessionId, token, tokenSecret);
        return sessionId;
    }
    
    protected String getSessionIdFromCookie(HttpServletRequest request){
        Cookie cookie =  WebUtils.getCookie(request, cookieIndexName);
        if(cookie == null){
            return null;
        }
        return cookie.getValue();
    }
    
    public int getTwitterCode() {
        return this.TWITTERCODE;
    }
}