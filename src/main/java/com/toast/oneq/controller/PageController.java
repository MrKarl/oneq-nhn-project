package com.toast.oneq.controller;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.toast.oneq.exception.ObjectStorageException;
import com.toast.oneq.redis.UserSessionDao;
import com.toast.oneq.service.FileUploadService;
import com.toast.oneq.util.CookieUtil;

/**
 * Copyright 2016 NHN Entertainment Corp. All rights Reserved. NHN Entertainment
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * Main Controller
 * 
 * @author ${email}
 */
@Controller
public class PageController {
    @Autowired
    private UserSessionDao userSessionDao;
    
    @Autowired
    private FileUploadService fileUploadService;

    /**
     * "/"로 접속했을 경우, main page
     * 
     * @param ${parameterDescription}
     * @return 다음으로 옮겨갈 페이지를 return한다.
     **/
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(Model model) {
        return "main";
    }
    
    @RequestMapping(value="/file/image/{fileName:.+}")
    @ResponseBody
    public byte[] loadFile(HttpServletResponse response, @PathVariable("fileName") String fileName) throws ObjectStorageException, IOException{
        InputStream in = null;
        byte[] retByte = null;
        try {
            in = fileUploadService.downloadFile(fileName);
            retByte = IOUtils.toByteArray(in);
            return retByte;
        } catch (IOException e) {
            throw new ObjectStorageException("file download failed");
        } finally{
            if(in != null){
                in.close();
            }
        }
    }

    /**
     * {Q_ID}에 해당하는 question을 보여주기 위한 메소드
     * 
     * @param ${parameterDescription}
     * @return 다음으로 옮겨갈 페이지를 return한다.
     **/
    @RequestMapping(value = "/v", method = RequestMethod.GET)
    public String questionView(Model model) {
        return "oneView";
    }
    
    @RequestMapping(value = "/i", method = RequestMethod.GET)
    public String iframeView() {
        return "iframe";
    }
    /**
     * "/tpl/{tpl}"- Backbone View 생성을 위한 HTML Template을 리턴하기 위한 메소드
     * 
     * @param tpl
     * @return template page
     */
    @RequestMapping(value = "/tpl/{tpl}", method = RequestMethod.GET)
    public String tplView(@PathVariable("tpl") String tpl) {
        return "/templates/" + tpl;
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String questionRegisterView(
            HttpServletResponse response,
            @CookieValue(value="uuid") String uuid){
        if(userSessionDao.isValidUuid(uuid)){
            return "question-create";
        }else{
            CookieUtil.unsetUuidToResponse(response, uuid);
            return "error";
        }
    }

    @RequestMapping(value = "/oauth")
    public String oauthView() {
        return "oauth";
    }
    
    @RequestMapping(value= "/logout")
    public String logout(
            HttpServletRequest request, 
            HttpServletResponse response,
            @CookieValue (value="uuid") String uuid) {
        if(userSessionDao.isValidUuid(uuid)){
            userSessionDao.deleteUuid(uuid);
        }
        CookieUtil.unsetUuidToResponse(response, uuid);
        return "redirect:/";
    }
    
    @RequestMapping(value = "/error", method = RequestMethod.GET)
    public String returnError() {
        return "error";
    }
    
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/ping", method = RequestMethod.GET)
    public void ping() {
    }
    
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/monitor/l7check", method = RequestMethod.GET)
    public String healthCheck() {
        return "l7check";
    }
}
