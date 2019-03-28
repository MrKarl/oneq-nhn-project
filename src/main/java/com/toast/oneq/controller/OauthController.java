package com.toast.oneq.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.toast.oneq.oauth.FacebookOauth;
import com.toast.oneq.oauth.PaycoOauth;
import com.toast.oneq.oauth.TwitterOauth;
import com.toast.oneq.redis.UserSessionDao;
import com.toast.oneq.service.UserService;
import com.toast.oneq.util.CookieUtil;
import com.toast.oneq.util.UuidUtil;
import com.toast.oneq.vo.UserVo;

import twitter4j.TwitterException;

@RequestMapping(value = "/oauth")
@Controller
public class OauthController {
    @Autowired
    private TwitterOauth twitterOauth;
    @Autowired
    private PaycoOauth paycoOauth;
    @Autowired
    private FacebookOauth facebookOauth;
    @Autowired
    private UserService userService;
    @Autowired
    private UserSessionDao userSessionDao;
    
    @RequestMapping(value = "/twitter/", method = RequestMethod.GET)
    public String redirectOauthTwitterUrl(HttpServletResponse response) throws TwitterException {
        return "redirect:" + twitterOauth.oauthTwitter(response);
    }
    
    @RequestMapping(value = "/twitter/callback", method = RequestMethod.GET)
    public String callbackTwitterOauth(HttpServletRequest request, HttpServletResponse response) throws TwitterException {
        UserVo user = twitterOauth.getUserInfo(request, response);
        int userId = userService.getValidUserId(user);
        CookieUtil.setUuidToResponse(response, assignUuid(userId));
        return "popupclose";
    }
    
    @RequestMapping(value = "/payco/", method = RequestMethod.GET)
    public String redirectOauthPaycoUrl(RedirectAttributes redirectAttributes) {
        return "redirect:" + paycoOauth.oauthPayco(redirectAttributes);
    }
    
    @RequestMapping(value = "/payco/callback", params = "code", method = RequestMethod.GET)
    public String callbackPaycoOauth(@RequestParam("code") String code, HttpServletResponse response,
            @RequestParam("state") String state) throws JsonParseException, JsonMappingException, IOException {
        UserVo user = paycoOauth.getUserInfo(code, state);
        int userId = userService.getValidUserId(user);
        CookieUtil.setUuidToResponse(response, assignUuid(userId));
        return "popupclose";
    }
    
    @RequestMapping(value = "/facebook/", method = RequestMethod.GET)
    public String redirectOauthFacebookUrl(RedirectAttributes redirectAttributes) {
        return "redirect:" + facebookOauth.oauthFacebook(redirectAttributes);
    }
    
    @RequestMapping(value = "/facebook/callback", params = "code", method = RequestMethod.GET)
    public String callbackFacebookOauth(HttpServletRequest request, HttpServletResponse response,
            @RequestParam("code") String code) throws JsonParseException, JsonMappingException, IOException {
        UserVo user = facebookOauth.getUserInfo(code);
        int userId = userService.getValidUserId(user);
        CookieUtil.setUuidToResponse(response, assignUuid(userId));
        return "popupclose";
    }
    
    public String assignUuid(int userId) {
        String uuid = UuidUtil.generatedUuid();
        userSessionDao.appendUserId(uuid, userId);
        return uuid;
    }
}
