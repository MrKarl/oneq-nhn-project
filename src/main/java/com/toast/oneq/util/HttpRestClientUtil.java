package com.toast.oneq.util;

import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class HttpRestClientUtil {
//    @Autowired
    private RestTemplate restTemplate;
    
    private HttpHeaders httpHeaders;
    private HttpEntity<?> httpEntity;    
    private ResponseEntity<?> responseEntity;
    private SimpleClientHttpRequestFactory simpleClientHttpRequestFactory;
    public HttpRestClientUtil(){
        simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        this.restTemplate = new RestTemplate();        
        this.httpHeaders = new HttpHeaders();
        simpleClientHttpRequestFactory.setBufferRequestBody(false);
        restTemplate.setRequestFactory(simpleClientHttpRequestFactory);
    }    
    
    public HttpRestClientUtil(HttpHeaders httpHeaders, HttpEntity<?> httpEntity, RestTemplate restTemplate){
        this.httpHeaders = httpHeaders;
        this.httpEntity = httpEntity;
        this.restTemplate = restTemplate;
    }
    
    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    public void setHeaders(String headerName, String headerValue){
        this.httpHeaders.add(headerName, headerValue);
    }
    
    public void setHeaders(Map<String, String> headerMap){
        headerMap.forEach((headerName, headerValue) -> {            
            this.httpHeaders.add(headerName, headerValue);
        });
    }
    
    public HttpHeaders getHeaders(){
        return httpHeaders;
    }

    public void setEntity(Object entity){
        this.httpEntity = new HttpEntity<>(entity, this.httpHeaders);
    }
    
    public HttpEntity<?> getHttpEntity(){
        return httpEntity;
    }
    
    public ResponseEntity<?> getResponseEntity(){
        return responseEntity;
    }
    
    public ResponseEntity<?> sendRestMethod(String url, String method){
        if(httpEntity == null){
            this.httpEntity = new HttpEntity<>(null, this.httpHeaders);
        }

        HttpMethod httpMethod = HttpMethod.valueOf(method.toUpperCase());
        responseEntity = restTemplate.exchange(url, httpMethod, httpEntity, String.class);
        return responseEntity;
        
        
//        final HttpMessageConverterExtractor<String> responseExtractor = new HttpMessageConverterExtractor<String>(
//                String.class, getMessageConverters());
//        RequestCallback requestCallback = new RequestCallback() {
//            
//            @Override
//            public void doWithRequest(ClientHttpRequest request) throws IOException {
//                // TODO Auto-generated method stub
//                
//            }
//        };
//        restTemplate.execute(url, method, requestCallback, responseExtractor);
        
    }
}
