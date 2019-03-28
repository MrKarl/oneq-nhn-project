package com.toast.oneq.vo;

import java.sql.Date;


/**
 * Copyright 2016 NHN Entertainment Corp. All rights Reserved.
 * NHN Entertainment PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * 유저의 정보를 저장하기 위한 VO
 * @author ${email}
 */
public class UserVo {

	private int userId;
	private int providerCode;
	private String oAuthId;
	private String	 token;
	private String	 userName;
	private Date birthDate;
	private String email;
	private int genderCode;
	private String thumbnailUrl;
	
    public int getUserId() {
        return userId;
    }
    public UserVo setUserId(int userId) {
        this.userId = userId;
        return this;
    }
    public int getProviderCode() {
        return providerCode;
    }
    public UserVo setProviderCode(int providerCode) {
        this.providerCode = providerCode;
        return this;
    }
    public String getOAuthId() {
        return oAuthId;
    }

    public UserVo setOAuthId(String oAuthId) {
        this.oAuthId = oAuthId;
        return this;
    }
    public String getToken() {
        return token;
    }
    public UserVo setToken(String token) {
        this.token = token;
        return this;
    }
    public String getUserName() {
        return userName;
    }
    public UserVo setUserName(String userName) {
        this.userName = userName;
        return this;
    }
    public Date getBirthDate() {
        return birthDate;
    }
    public UserVo setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
        return this;
    }
    public String getEmail() {
        return email;
    }
    public UserVo setEmail(String email) {
        this.email = email;
        return this;
    }
    public int getGenderCode() {
        return genderCode;
    }
    public UserVo setGenderCode(int genderCode) {
        this.genderCode = genderCode;
        return this;
    }
    public String getThumbnailUrl() {
        return thumbnailUrl;
    }
    public UserVo setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
        return this;
    }
	
	
}
