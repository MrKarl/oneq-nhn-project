package com.toast.oneq.vo;

public class VoteVo {
    private int questionId;
    private int userId;
    private int itemId;
    private int questionTypeCode;
    private String refererHostUrl;
    
    public String getRefererHostUrl() {
        return refererHostUrl;
    }
    public VoteVo setRefererHostUrl(String refererHostUrl) {
        this.refererHostUrl = refererHostUrl;
        return this;
    }
    public int getQuestionId() {
        return questionId;
    }
    public VoteVo setQuestionId(int questionId) {
        this.questionId = questionId;
        return this;
    }
    public int getUserId() {
        return userId;
    }
    public VoteVo setUserId(int userId) {
        this.userId = userId;
        return this;
    }
    public int getItemId() {
        return itemId;
    }
    public VoteVo setItemId(int itemId) {
        this.itemId = itemId;
        return this;
    }
    
    public int getQuestionTypeCode() {
        return questionTypeCode;
    }
    public VoteVo setQuestionTypeCode(int questionTypeCode) {
        this.questionTypeCode = questionTypeCode;
        return this;
    }
    
    public boolean isSingleVoteType(){
        if(this.questionTypeCode == 0){
            return true;
        } else{
            return false;
        }
    }
    
    public boolean isMultiVoteType(){
        if(this.questionTypeCode == 1){
            return true;
        } else{
            return false;
        }
    }
}
