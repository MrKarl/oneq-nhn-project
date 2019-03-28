package com.toast.oneq.vo;

/**
 * Copyright 2016 NHN Entertainment Corp. All rights Reserved. NHN Entertainment
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * 항목 정보를 저장하기 위한 VO
 * 
 * @author ${email}
 */
public class ItemVo {
    
	private int itemId;
	private int questionId;
	private int mediaTypeCode;
	private int resultCount;
	private int isVoted;
	private String title;
	private String mediaPath;
	
    public int getItemId() {
        return itemId;
    }
    public ItemVo setItemId(int itemId) {
        this.itemId = itemId;
        return this;
    }

    public int getQuestionId() {
        return questionId;
    }
    public ItemVo setQuestionId(int questionId) {
        this.questionId = questionId;
        return this;
    }
    
    public String getTitle() {
        return title;
    }

    public ItemVo setTitle(String title) {
        this.title = title;
        return this;
    }

    public int getMediaTypeCode() {
        return mediaTypeCode;
    }
    public ItemVo setMediaTypeCode(int mediaTypeCode) {
        this.mediaTypeCode = mediaTypeCode;
        return this;
    }

    public String getMediaPath() {
        return mediaPath;
    }

    public ItemVo setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath;
        return this;
    }

    public int getResultCount() {
        return resultCount;
    }

    public ItemVo setResultCount(int resultCount) {
        this.resultCount = resultCount;
        return this;
    }

    public int getIsVoted() {
        return isVoted;
    }

    public ItemVo setIsVoted(int isVoted) {
        this.isVoted = isVoted;
        return this;
    }
    
    public boolean isMediaTypeImage(){
        return this.mediaTypeCode == 1;
    }
    
    public boolean isMediaPathEmpty(){
        return (this.mediaPath != null && this.mediaPath.length() == 0);
    }
}
