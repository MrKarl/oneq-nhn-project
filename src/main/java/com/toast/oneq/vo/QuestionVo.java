package com.toast.oneq.vo;

import java.sql.Date;
import java.util.List;

/**
 * Copyright 2016 NHN Entertainment Corp. All rights Reserved.
 * NHN Entertainment PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * 질문 정보를 저장하기 위한 VO
 * @author ${email}
 */
public class QuestionVo {
    
	private int questionId;
	private int userId;
	private int questionTypeCode;
	private int hit;
	private int mediaTypeCode;
	private int voteUserCountMax;
	private int voteUserCount;
	private String title;
	private String content;
	private String mediaPath;
	private Date startAt;
	private Date stopAt;
	private Date createdAt;
	private Date finishedAt;
	private List<ItemVo> items;
	private List<HashVo> hashs;
	
	public int getQuestionId() {
        return questionId;
    }
    public QuestionVo setQuestionId(int questionId) {
        this.questionId = questionId;
        return this;
    }
    
    public int getUserId() {
        return userId;
    }
    public QuestionVo setUserId(int userId) {
        this.userId = userId;
        return this;
    }
    
    public String getTitle() {
        return title;
    }
    public QuestionVo setTitle(String title) {
        this.title = title;
        return this;
    }
    
    public String getContent() {
        return content;
    }
    public QuestionVo setContent(String content) {
        this.content = content;
        return this;
    }
    
    public int getQuestionTypeCode() {
        return questionTypeCode;
    }
    public QuestionVo setQuestionTypeCode(Byte questionTypeCode) {
        this.questionTypeCode = questionTypeCode;
        return this;
    }
    
    public int getHit() {
        return hit;
    }
    public QuestionVo setHit(int hit) {
        this.hit = hit;
        return this;
    }
    
    public int getMediaTypeCode() {
        return mediaTypeCode;
    }
    public QuestionVo setMediaTypeCode(Byte mediaTypeCode) {
        this.mediaTypeCode = mediaTypeCode;
        return this;
    }
    
    public String getMediaPath() {
        return mediaPath;
    }
    public QuestionVo setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath;
        return this;
    }
    
    public Date getStartAt() {
        return startAt;
    }
    public QuestionVo setStartAt(Date startAt) {
        this.startAt = startAt;
        return this;
    }
    
    public Date getStopAt() {
        return stopAt;
    }
    public QuestionVo setStopAt(Date stopAt) {
        this.stopAt = stopAt;
        return this;
    }
    
    public int getVoteUserCountMax() {
        return voteUserCountMax;
    }
    public QuestionVo setVoteUserCountMax(int voteUserCountMax) {
        this.voteUserCountMax = voteUserCountMax;
        return this;
    }
    
    public int getVoteUserCount() {
        return voteUserCount;
    }
    public QuestionVo setVoteUserCount(int voteUserCount) {
        this.voteUserCount = voteUserCount;
        return this;
    }
    
    public Date getCreatedAt() {
        return createdAt;
    }
    public QuestionVo setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }
    
    public Date getFinishedAt() {
        return finishedAt;
    }
    public QuestionVo setFinishedAt(Date finishedAt) {
        this.finishedAt = finishedAt;
        return this;
    }
    
    public List<ItemVo> getItems() {
		return items;
	}
	public QuestionVo setItems(List<ItemVo> items) {
		this.items = items;
		return this;
	}
	public List<HashVo> getHashs() {
	    return hashs;
	}
	public QuestionVo setHashs(List<HashVo> hashs) {
	    this.hashs = hashs;
	    return this;
	}
	
	public boolean isMediaTypeImage(){
	    if(this.mediaTypeCode == 1){
	        return true;
	    }else{
	        return false;
	    }
	}
	
	public boolean isMediaPathEmpty(){
        if(this.mediaPath != null && this.mediaPath.length() == 0){
            return true;
        }else{
            return false;
        }
    }
}
