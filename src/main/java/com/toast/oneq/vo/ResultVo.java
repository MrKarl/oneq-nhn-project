package com.toast.oneq.vo;

import java.security.Timestamp;


/**
 * Copyright 2016 NHN Entertainment Corp. All rights Reserved.
 * NHN Entertainment PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * 결과 정보를 저장하기 위한 VO
 * @author ${email}
 */
public class ResultVo {
    
	private int itemId;
	private int userId;
	private int resultValue;
	private Timestamp registeredAt;
	private String refererHostUrl;
	
	public ResultVo(){}
	
    public int getItemId() {
        return itemId;
    }
    public ResultVo setItemId(int itemId) {
        this.itemId = itemId;
        return this;
    }
    
    public int getUserId() {
        return userId;
    }
    public ResultVo setUserId(int userId) {
        this.userId = userId;
        return this;
    }
    
    public int getResultValue() {
        return resultValue;
    }
    public ResultVo setResultValue(int resultValue) {
        this.resultValue = resultValue;
        return this;
    }
    
    public Timestamp getRegisteredAt() {
        return registeredAt;
    }
    public ResultVo setRegisteredAt(Timestamp registeredAt) {
        this.registeredAt = registeredAt;
        return this;
    }
    
    public String getRefererHostUrl() {
        return refererHostUrl;
    }
    public ResultVo setRefererHostUrl(String refererHostUrl) {
        this.refererHostUrl = refererHostUrl;
        return this;
    }
}
