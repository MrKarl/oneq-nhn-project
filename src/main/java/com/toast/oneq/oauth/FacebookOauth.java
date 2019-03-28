package com.toast.oneq.oauth;

import java.io.IOException;
import java.net.URL;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.toast.oneq.vo.FacebookVo;
import com.toast.oneq.vo.UserVo;


public class FacebookOauth {
    
    private String SCOPE;
    private String REDIRECT_URI;
    private String CLIENT_ID;
    private String APP_SECRET;
    private String DIALOG_OAUTH;
    private String ACCESS_TOKEN;
    private String ME;
    private String FIELDS;
    private int FACKBOOKCODE = 2;
    
    public FacebookOauth(String scope, String redirectUri, String clientId, String appSecret
            , String dialogOauth, String accessToken, String me, String fields) {
        this.SCOPE = scope;
        this.REDIRECT_URI = redirectUri;
        this.CLIENT_ID = clientId;
        this.APP_SECRET = appSecret;
        this.DIALOG_OAUTH = dialogOauth;
        this.ACCESS_TOKEN = accessToken;
        this.ME = me;
        this.FIELDS = fields;
    }
    public String oauthFacebook(RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("client_id", CLIENT_ID);
        redirectAttributes.addAttribute("redirect_uri", REDIRECT_URI);
        redirectAttributes.addAttribute("scope", SCOPE);
        return DIALOG_OAUTH;
    }
    
    public UserVo getUserInfo(String code) throws JsonParseException, JsonMappingException, IOException {
        FacebookVo facebookVo = this.getAccessToken(code);
        facebookVo = this.getUserIdNName(facebookVo);
        String oauthId = facebookVo.getId();
        String userName = facebookVo.getName();
        
        return new UserVo().setOAuthId(oauthId)
                .setUserName(userName)
                .setProviderCode(FACKBOOKCODE);
    }
    
    protected FacebookVo getAccessToken(String code) throws JsonParseException, JsonMappingException, IOException {
        URL accessTokenUrl = new URL(ACCESS_TOKEN + "?client_id="+CLIENT_ID+"&redirect_uri="+REDIRECT_URI+"&client_secret="+APP_SECRET+"&code="+code);
        return new ObjectMapper().readValue(accessTokenUrl, FacebookVo.class);
    }
    
    protected FacebookVo getUserIdNName(FacebookVo facebookVo) throws JsonProcessingException, IOException {
        URL meUrl = new URL(ME+"?fields="+FIELDS+"&access_token="+facebookVo.getAccess_token());
        ObjectReader updater = new ObjectMapper().readerForUpdating(facebookVo);
        return updater.readValue(meUrl);
    }
    
    public int getFacebookCode() {
        return this.FACKBOOKCODE;
    }
}
