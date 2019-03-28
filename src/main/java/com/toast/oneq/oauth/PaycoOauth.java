package com.toast.oneq.oauth;

import java.io.IOException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.toast.oneq.vo.PaycoVo;
import com.toast.oneq.vo.UserVo;


public class PaycoOauth {
    @Autowired
    private RestTemplate restTemplate;
    
    private String CLIENT_ID;
    private String RESPONSE_TYPE;
    private String APP_SECRET;
    private String SERVICE_PROVIDER_CODE;
    private String REDIRECT_URI;
    private String STATE;
    private String REQUEST_CODE;
    private String GRANT_TYPE;
    private String ACCESS_TOKEN;
    private String GET_PROFILE;
    private int PAYCOCODE;
    
    public PaycoOauth(String clientId, String responseType, String appSecret
            , String serviceProvider, String redirectUri, String requestCode
            , String grantType, String accessToken, String getProfile, int paycoCode) {
        this.CLIENT_ID = clientId;
        this.RESPONSE_TYPE = responseType;
        this.APP_SECRET = appSecret;
        this.SERVICE_PROVIDER_CODE = serviceProvider;
        this.REDIRECT_URI = redirectUri;
        this.REQUEST_CODE = requestCode;
        this.GRANT_TYPE = grantType;
        this.ACCESS_TOKEN = accessToken;
        this.GET_PROFILE = getProfile;
        this.PAYCOCODE = paycoCode;
        this.STATE = "cv1564czvc0dss";
    }
    
    public String oauthPayco(RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("response_type", RESPONSE_TYPE);
        redirectAttributes.addAttribute("client_id", CLIENT_ID);
        redirectAttributes.addAttribute("redirect_uri", REDIRECT_URI);
        redirectAttributes.addAttribute("state", STATE);
        redirectAttributes.addAttribute("serviceProviderCode", SERVICE_PROVIDER_CODE);
       
        return REQUEST_CODE;
    }
    
    public UserVo getUserInfo(String code, String state) throws JsonParseException, JsonMappingException, IOException {
        PaycoVo paycoVo = this.getAccessToken(code);
        paycoVo = this.getUserId(paycoVo.getAccess_token());
        String oauthId = paycoVo.getProfileBasic().getIdNo();
        return new UserVo().setOAuthId(oauthId)
                            .setProviderCode(PAYCOCODE);
    }
    
    protected PaycoVo getAccessToken(String code) throws JsonParseException, JsonMappingException, IOException {
        URL accessTokenUrl = new URL(ACCESS_TOKEN + "?grant_type="+GRANT_TYPE+
                "&client_id="+CLIENT_ID+"&client_secret="+APP_SECRET+
                "&code="+code+"&state="+STATE+"&logoutClientIdList="+CLIENT_ID);
        return new ObjectMapper().readValue(accessTokenUrl,  PaycoVo.class);
    }
    
    protected PaycoVo getUserId(String accessToken) {
        String jsonStringToken = "{"
                + "\"client_id\" : \""+ CLIENT_ID+"\","
                + "\"access_token\" : \""+accessToken
           +"\"}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType( MediaType.APPLICATION_JSON );
        HttpEntity<String> request = new HttpEntity<String>( jsonStringToken , headers );
        return restTemplate.postForObject(GET_PROFILE, request, PaycoVo.class);
    }
    
    public int getPaycoCode() {
        return this.PAYCOCODE;
    }
}
