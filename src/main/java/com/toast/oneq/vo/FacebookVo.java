package com.toast.oneq.vo;

public class FacebookVo {
    private String access_token;
    private String token_type;
    private String expires_in;
    private String id;
    private String name;
    public String getAccess_token() {
        return access_token;
    }
    public FacebookVo setAccess_token(String access_token) {
        this.access_token = access_token;
        return this;
    }
    public String getToken_type() {
        return token_type;
    }
    public FacebookVo setToken_type(String token_type) {
        this.token_type = token_type;
        return this;
    }
    public String getExpires_in() {
        return expires_in;
    }
    public FacebookVo setExpires_in(String expires_in) {
        this.expires_in = expires_in;
        return this;
    }
    public String getId() {
        return id;
    }
    public FacebookVo setId(String id) {
        this.id = id;
        return this;
    }
    public String getName() {
        return name;
    }
    public FacebookVo setName(String name) {
        this.name = name;
        return this;
    }
}
