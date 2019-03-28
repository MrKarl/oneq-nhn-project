package com.toast.oneq.storage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import com.toast.oneq.exception.ObjectStorageException;
import com.toast.oneq.util.HttpRestClientUtil;

public class ObjectStorage {
    private String accessToken;
    private LocalDateTime expiresAt;
    
    private String tenantName;
    private String tenantId;
    private String account;
    private String requestTokenUrl;
    private String userName;
    private String password;
    public  String defaultContainer; 
    public  String objectStoreUrl;
    
    public ObjectStorage(
            String tenantName, String tenantId, 
            String account, String requestTokenUrl, 
            String userName, String password, 
            String defaultContainer, String objectStoreUrl) {
        this.tenantName = tenantName;
        this.tenantId = tenantId;
        this.account = account;
        this.requestTokenUrl = requestTokenUrl;
        this.userName = userName;
        this.password = password;
        this.defaultContainer = defaultContainer;
        this.objectStoreUrl = objectStoreUrl;
    }
    
    public String getDefaultContainer() {
        return defaultContainer;
    }

    public void setDefaultContainer(String defaultContainer) {
        this.defaultContainer = defaultContainer;
    }
    
    public String getObjectStoreUrl() {
        return objectStoreUrl;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
    
    protected boolean isValidAccessTokenWithExpires() {
        LocalDateTime nowDateTime = LocalDateTime.now(ZoneId.of("UTC"));
        
        if (expiresAt == null) {
            return false;
        }

        if (expiresAt.compareTo(nowDateTime) <= 0) {
            return false;
        }

        return true;
    }
    
    protected String receiveAccessToken() throws ObjectStorageException {
        try {
            Map<String, Object> requestMap = new HashMap<String, Object>();
            Map<String, Object> authMap = new HashMap<String, Object>();
            Map<String, String> passwordCredentialsMap = new HashMap<String, String>();

            passwordCredentialsMap.put("password", password);
            passwordCredentialsMap.put("username", userName);
            authMap.put("tenantName", tenantName);
            authMap.put("passwordCredentials", passwordCredentialsMap);
            requestMap.put("auth", authMap);

            HttpRestClientUtil httpRestClientUtil = new HttpRestClientUtil();
            Map<String, String> headerMap = new HashMap<String, String>();
            headerMap.put("Content-Type", "application/json");
            httpRestClientUtil.setHeaders(headerMap);
            httpRestClientUtil.setEntity(requestMap);
            httpRestClientUtil.sendRestMethod(requestTokenUrl, "POST");
            String responseStr = httpRestClientUtil.getResponseEntity().getBody().toString();

            JSONObject responseJson;
            responseJson = new JSONObject(responseStr);
            String datePattern = "yyyy-MM-dd'T'HH:mm:ss'Z'";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);
            LocalDateTime expires = LocalDateTime
                    .parse(responseJson.getJSONObject("access").getJSONObject("token").getString("expires"), formatter);
            String accessToken = responseJson.getJSONObject("access").getJSONObject("token").getString("id");

            setAccessToken(accessToken);
            setExpiresAt(expires);
            return accessToken;
        } catch (JSONException e) {
            throw new ObjectStorageException("Access Token JSON Parsing Exception");
        }
    }
    
    public String uploadObject(String fileName, MultipartFile file) throws ObjectStorageException, IOException{
        if (!isValidAccessTokenWithExpires()) {
            receiveAccessToken();
        }
        
        OutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            String mimeType = file.getContentType();
            String objUploadPathUrl = objectStoreUrl + defaultContainer + fileName;
            inputStream = file.getInputStream();
            URL url = new URL(objUploadPathUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestProperty("Content-Type", mimeType);
            httpURLConnection.setRequestProperty("X-Auth-Token", accessToken);
            httpURLConnection.setChunkedStreamingMode(10000);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod("PUT");
            outputStream = httpURLConnection.getOutputStream();
            IOUtils.copy(inputStream, outputStream);
            
            if (httpURLConnection.getResponseCode() != 201) {
                String errorMessage = "ObjectStorage FileUpload Response Code is not 201. is "+ httpURLConnection.getResponseCode();
                throw new ObjectStorageException(errorMessage);
            }
            httpURLConnection.disconnect();
            
            return fileName;
        } catch (Exception e) {
            throw new ObjectStorageException(e);
        } finally {
            if(inputStream != null){
                inputStream.close();
            }
            
            if(outputStream != null){
                outputStream.close();
            }
        }
    }
    
    
    public InputStream downloadObject(String filePath) throws ObjectStorageException, IOException{
        if (!isValidAccessTokenWithExpires()) {
            receiveAccessToken();
        }
        
        InputStream inputStream = null;
        try {
            URL url = new URL(filePath);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestProperty("X-Auth-Token", accessToken);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod("GET");
            inputStream = httpURLConnection.getInputStream();
            return inputStream;
        } catch (Exception e) {
            throw new ObjectStorageException(e);
        }
    }
    
    public int deleteObject(String filePath) throws ObjectStorageException, IOException{
        if (!isValidAccessTokenWithExpires()) {
            receiveAccessToken();
        }
        
        try {
            URL url = new URL(filePath);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestProperty("X-Auth-Token", accessToken);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod("DELETE");

            if (httpURLConnection.getResponseCode() != 204) {
                throw new ObjectStorageException("ObjectStorage FileDelete Response Code is not 204");
            }

            int responseCode = httpURLConnection.getResponseCode();
            httpURLConnection.disconnect();
            
            return responseCode;
        } catch (Exception e) {
            throw new ObjectStorageException(e);
        }
    }
}

