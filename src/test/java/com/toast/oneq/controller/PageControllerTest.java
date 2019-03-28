package com.toast.oneq.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.InputStream;

import javax.servlet.http.Cookie;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.toast.oneq.redis.UserSessionDao;
import com.toast.oneq.service.FileUploadService;

@RunWith(MockitoJUnitRunner.class)
public class PageControllerTest {
    @Mock
    private UserSessionDao userSessionDao;
    
    @Mock
    private FileUploadService fileUploadService;
    
    @InjectMocks
    private PageController pageController;
    
    private MockMvc mockMvc;
    
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        
//        oauthView를 위한 설정
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/jsp/view/");
        viewResolver.setSuffix(".jsp");
        
        mockMvc = MockMvcBuilders
                    .standaloneSetup(pageController)
                    .setViewResolvers(viewResolver)
                    .build();
    }
    
    @Test
    public void homeTest() throws Exception {
        mockMvc.perform(get("/")).andExpect(status().isOk());
    }
    
    @Test
    public void questionViewTest() throws Exception {
        mockMvc.perform(get("/v")).andExpect(status().isOk());
    }
    
    @Test
    public void iframeViewTest() throws Exception {
        mockMvc.perform(get("/i")).andExpect(status().isOk());
    }
    
    @Test
    public void tplViewTest() throws Exception {
        mockMvc.perform(get("/tpl/question")).andExpect(status().isOk());
    }
    
    @Test
    public void questionRegisterViewTest() throws Exception {
        when(userSessionDao.isValidUuid("1")).thenReturn(true);
        when(userSessionDao.isValidUuid("2")).thenReturn(false);
        mockMvc.perform(get("/create").cookie(new Cookie("uuid","1"))).andExpect(status().isOk());
        mockMvc.perform(get("/create").cookie(new Cookie("uuid","2"))).andExpect(status().isOk());
    }
    
    @Test
    public void oauthViewTest() throws Exception {
        mockMvc.perform(get("/oauth")).andExpect(status().isOk());
    }
    
    @Test
    public void logoutTest() throws Exception {
        when(userSessionDao.isValidUuid("1")).thenReturn(true);
        when(userSessionDao.isValidUuid("2")).thenReturn(false);
        mockMvc.perform(get("/logout").cookie(new Cookie("uuid","1")))
                    .andExpect(status().is3xxRedirection());
    }
    @Test
    public void pingTest() throws Exception {
        mockMvc.perform(get("/ping")).andExpect(status().isOk());
    }
    
    @Test
    public void healthCheckTest() throws Exception {
        mockMvc.perform(get("/monitor/l7check")).andExpect(status().isOk());
    }
    
    @Test
    public void returnErrorTest() throws Exception {
        mockMvc.perform(get("/error")).andExpect(status().isOk());
    }
    
    @Test
    public void loadFileTest() throws Exception {
        String fileName = "ACifb_20160304170313.jpg";
        InputStream inputStream = IOUtils.toInputStream("some test data for my input stream");
        Mockito.when(fileUploadService.downloadFile(fileName)).thenReturn(inputStream);
        mockMvc.perform(get("/file/image/"+fileName)).andExpect(status().isOk());
        inputStream.close();
    }
}
