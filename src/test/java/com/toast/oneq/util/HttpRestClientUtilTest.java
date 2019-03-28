package com.toast.oneq.util;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import com.toast.oneq.exception.FileUploadException;

import twitter4j.JSONException;
import twitter4j.JSONObject;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextHierarchy({
    @ContextConfiguration("/spring/application-context.xml"),
    @ContextConfiguration("/spring/servlet-context.xml")
})
public class HttpRestClientUtilTest {
    private HttpRestClientUtil httpRestClientUtil;
    private final static String TENANT_ID_ALPHA = "a1fd1f2df1584962b6a00c01becf228a";
    private final static String ACCOUNT_ALPHA = "AUTH_"+TENANT_ID_ALPHA;
    private final static String IDENTITY = "https://api-compute.cloud.toast.com/identity/v2.0";
    private final static String OBJECT_STORE_ALPHA = "https://api-storage.cloud.toast.com/v1/"+ACCOUNT_ALPHA;
    private final static String CONTAINER_NAME = "oneq_storage";
    private final static String REQUEST_TOKEN_URL = IDENTITY+"/tokens";
    
    @Before
    public void setUP(){
        httpRestClientUtil = new HttpRestClientUtil();
    }
    
    @Test
    public void setHeadersTest_헤더값맵으로세팅(){
        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("Content-Type", "application/json");
        httpRestClientUtil.setHeaders(headerMap);
        HttpHeaders httpHeaders = new HttpHeaders();
        headerMap.forEach((k, v) -> httpHeaders.add(k, v));
        assertEquals(httpRestClientUtil.getHeaders(), httpHeaders);
    }
    
    @Test
    public void setHeadersTest_헤더값키밸류로세팅(){
        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("Content-Type", "application/json");
        HttpHeaders httpHeaders = new HttpHeaders();
        headerMap.forEach((k, v) -> {
            httpHeaders.add(k, v);
            httpRestClientUtil.setHeaders(k, v);
        });
        assertEquals(httpRestClientUtil.getHeaders(), httpHeaders);
    }
    
    @Test
    public void setHeadersTest_헤더및Entity설정(){
        String requestBody = "{\"auth\":{\"tenantName\":\"wyYYXhjX\",\"passwordCredentials\":{\"password\":\"oneq\",\"username\":\"panki.park@nhnent.com\"}}}";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<String>(requestBody, headers);
        RestTemplate restTemplate = new RestTemplate();
        
        httpRestClientUtil = new HttpRestClientUtil(headers, entity, restTemplate);
        
        assertEquals(httpRestClientUtil.getHeaders(), headers);
        assertEquals(httpRestClientUtil.getHttpEntity(), entity);        
    }

    
    @Test
    public void sendRestMethodTest_POST테스트1() throws JSONException, FileUploadException{
        String requestBody = "{\"auth\":{\"tenantName\":\"wyYYXhjX\",\"passwordCredentials\":{\"password\":\"oneq\",\"username\":\"panki.park@nhnent.com\"}}}";        
        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("Content-Type", "application/json");
        httpRestClientUtil.setHeaders(headerMap);
        httpRestClientUtil.setEntity(requestBody);        

        ResponseEntity<?> responseEntity = httpRestClientUtil.sendRestMethod(REQUEST_TOKEN_URL, "POST");
        String responseStr = responseEntity.getBody().toString();
        JSONObject responseJson = new JSONObject(responseStr);
        String tenantId= responseJson.getJSONObject("access").getJSONObject("token").getJSONObject("tenant").getString("id");
        
        
        assertEquals(tenantId, TENANT_ID_ALPHA);
    }
    
    @Test
    public void sendRestMethodTest_POST테스트2() throws JSONException, FileUploadException{
        String requestBody = "{\"auth\":{\"tenantName\":\"wyYYXhjX\",\"passwordCredentials\":{\"password\":\"oneq\",\"username\":\"panki.park@nhnent.com\"}}}";
        
        Map<String, Object> requestMap = new HashMap<String, Object>();
        Map<String, Object> authMap = new HashMap<String, Object>();
        Map<String, String> passwordCredentialsMap = new HashMap<String, String>();
        
        passwordCredentialsMap.put("password", "oneq");
        passwordCredentialsMap.put("username", "panki.park@nhnent.com");
        authMap.put("tenantName", "wyYYXhjX");
        authMap.put("passwordCredentials", passwordCredentialsMap);
        requestMap.put("auth", authMap);
        
        
        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("Content-Type", "application/json");
        httpRestClientUtil.setHeaders(headerMap);
        httpRestClientUtil.setEntity(requestBody);

        ResponseEntity<?> responseEntity = httpRestClientUtil.sendRestMethod(REQUEST_TOKEN_URL, "POST");
        String responseStr = responseEntity.getBody().toString();
        JSONObject responseJson = new JSONObject(responseStr);
        String tenantId= responseJson.getJSONObject("access").getJSONObject("token").getJSONObject("tenant").getString("id");
        
        assertEquals(tenantId, TENANT_ID_ALPHA);
    }
    
    
    @Test
    public void sendRestMethodTest_PUT테스트() throws JSONException, FileUploadException{
        String requestBody = "{\"auth\":{\"tenantName\":\"wyYYXhjX\",\"passwordCredentials\":{\"password\":\"oneq\",\"username\":\"panki.park@nhnent.com\"}}}";
        
        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("Content-Type", "application/json");
        httpRestClientUtil.setHeaders(headerMap);
        httpRestClientUtil.setEntity(requestBody);        

        ResponseEntity<?> responseEntity = httpRestClientUtil.sendRestMethod(REQUEST_TOKEN_URL, "POST");
        String responseStr = responseEntity.getBody().toString();
        JSONObject responseJson = new JSONObject(responseStr);
        String accessToken = responseJson.getJSONObject("access").getJSONObject("token").getString("id");
        
        HttpRestClientUtil httpRestClientUtil2 = new HttpRestClientUtil();
        String fileName = "testTextFile2222.txt";
        String mimeType = "text/plain";
        byte[] fileData = {111,2,33,44,55,66,7,8,9,0,0,0,0,1};
        String objUploadPathUrl = OBJECT_STORE_ALPHA+"/"+CONTAINER_NAME+"/"+fileName;
        
        Map<String, String> headerMap2 = new HashMap<String, String>();
        headerMap2.put("Content-Type", mimeType);
        headerMap2.put("X-Auth-Token", accessToken);
        httpRestClientUtil2.setHeaders(headerMap2);
        httpRestClientUtil2.setEntity(fileData);
        
        
        ResponseEntity<?> responseEntity2 = httpRestClientUtil2.sendRestMethod(objUploadPathUrl, "PUT");
        HttpStatus httpResponseCode = responseEntity2.getStatusCode();
        
        assertEquals(httpResponseCode.value(), 201);
    }
    
    @Test
    public void sendRestMethodTest_DELETE테스트() throws JSONException, FileUploadException{
        String requestBody = "{\"auth\":{\"tenantName\":\"wyYYXhjX\",\"passwordCredentials\":{\"password\":\"oneq\",\"username\":\"panki.park@nhnent.com\"}}}";
        
        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("Content-Type", "application/json");
        httpRestClientUtil.setHeaders(headerMap);
        httpRestClientUtil.setEntity(requestBody);        

        ResponseEntity<?> responseEntity = httpRestClientUtil.sendRestMethod(REQUEST_TOKEN_URL, "POST");
        String responseStr = responseEntity.getBody().toString();
        JSONObject responseJson = new JSONObject(responseStr);
        String accessToken = responseJson.getJSONObject("access").getJSONObject("token").getString("id");
        
        HttpRestClientUtil httpRestClientUtil2 = new HttpRestClientUtil();
        String fileName = "testTextFile33333333333.txt";
        String mimeType = "text/plain";
        byte[] fileData = {111,2,33,44,55,66,7,8,9,0,0,0,0,1};
        String objUploadPathUrl = OBJECT_STORE_ALPHA+"/"+CONTAINER_NAME+"/"+fileName;
        
        Map<String, String> headerMap2 = new HashMap<String, String>();
        headerMap2.put("Content-Type", mimeType);
        headerMap2.put("X-Auth-Token", accessToken);
        httpRestClientUtil2.setHeaders(headerMap2);
        httpRestClientUtil2.setEntity(fileData);        
        
        ResponseEntity<?> responseEntity2 = httpRestClientUtil2.sendRestMethod(objUploadPathUrl, "PUT");
        HttpStatus httpResponseCode = responseEntity2.getStatusCode();
        
        assertEquals(httpResponseCode.value(), 201);
        
        HttpRestClientUtil httpRestClientUtil3 = new HttpRestClientUtil();
        Map<String, String> headerMap3 = new HashMap<String, String>();
        headerMap3.put("X-Auth-Token", accessToken);
        httpRestClientUtil3.setHeaders(headerMap3);

        ResponseEntity<?> responseEntity3 = httpRestClientUtil3.sendRestMethod(objUploadPathUrl, "DELETE");
        httpResponseCode = responseEntity3.getStatusCode();
        
        assertEquals(httpResponseCode.value(), 204);
    }
    
    @Test
    public void sendRestMethodTest5() {
        String requestBody = "{\"auth\":{\"tenantName\":\"wyYYXhjX\",\"passwordCredentials\":{\"password\":\"oneq\",\"username\":\"panki.park@nhnent.com\"}}}";
        
        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("Content-Type", "application/json");
        httpRestClientUtil.setHeaders(headerMap);
        httpRestClientUtil.setEntity(requestBody);        

        ResponseEntity<?> responseEntity = httpRestClientUtil.sendRestMethod(REQUEST_TOKEN_URL, "POST");
    }
}
