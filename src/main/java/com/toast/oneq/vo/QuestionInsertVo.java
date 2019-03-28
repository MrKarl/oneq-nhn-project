package com.toast.oneq.vo;

import java.sql.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class QuestionInsertVo {
    private int            questionId;
    private int            userId;
    private int            questionTypeCode;
    private int            hit;
    private int            mediaTypeCode;
    private int            voteUserCountMax;
    private int            voteUserCount;
    private String         title;
    private String         content;
    private String         mediaPath;
    private Date           startAt;
    private Date           stopAt;
    private Date           createdAt;
    private Date           finishedAt;
    
    private List<String>   itemTitle;
    private List<Integer>  itemMediaTypeCode;
    private List<String>   itemMediaPath;
    private List<String>   hashName;
    
    private MultipartFile           questionMediaFile;
    private List<MultipartFile>     itemMediaFiles;
    
    private List<ItemVo> itemVoList;
    
    
    public int getQuestionId() {
        return questionId;
    }
    public QuestionInsertVo setQuestionId(int item_media_type) {
        this.questionId = item_media_type;
        return this;
    }
    
    public int getUserId() {
        return userId;
    }
    public QuestionInsertVo setUserId(int userId) {
        this.userId = userId;
        return this;
    }
    
    public String getTitle() {
        return title;
    }
    public QuestionInsertVo setTitle(String title) {
        this.title = title;
        return this;
    }
    
    public String getContent() {
        return content;
    }
    public QuestionInsertVo setContent(String content) {
        this.content = content;
        return this;
    }
    
    public int getQuestionTypeCode() {
        return questionTypeCode;
    }
    public QuestionInsertVo setQuestionTypeCode(Byte questionTypeCode) {
        this.questionTypeCode = questionTypeCode;
        return this;
    }
    
    public int getHit() {
        return hit;
    }
    public QuestionInsertVo setHit(int hit) {
        this.hit = hit;
        return this;
    }
    
    public int getMediaTypeCode() {
        return mediaTypeCode;
    }
    public QuestionInsertVo setMediaTypeCode(Byte mediaTypeCode) {
        this.mediaTypeCode = mediaTypeCode;
        return this;
    }
    
    public String getMediaPath() {
        return mediaPath;
    }
    public QuestionInsertVo setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath;
        return this;
    }
    
    public Date getStartAt() {
        return startAt;
    }
    public QuestionInsertVo setStartAt(Date startAt) {
        this.startAt = startAt;
        return this;
    }
    
    public Date getStopAt() {
        return stopAt;
    }
    public QuestionInsertVo setStopAt(Date stopAt) {
        this.stopAt = stopAt;
        return this;
    }
    
    public int getVoteUserCountMax() {
        return voteUserCountMax;
    }
    public QuestionInsertVo setVoteUserCountMax(int voteUserCountMax) {
        this.voteUserCountMax = voteUserCountMax;
        return this;
    }
    
    public int getVoteUserCount() {
        return voteUserCount;
    }
    public QuestionInsertVo setVoteUserCount(int voteUserCount) {
        this.voteUserCount = voteUserCount;
        return this;
    }
    
    public Date getCreatedAt() {
        return createdAt;
    }
    public QuestionInsertVo setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }
    
    public Date getFinishedAt() {
        return finishedAt;
    }
    public QuestionInsertVo setFinishedAt(Date finishedAt) {
        this.finishedAt = finishedAt;
        return this;
    }
    
    public List<String> getItemTitle() {
        return itemTitle;
    }
    public QuestionInsertVo setItemTitle(List<String> itemTitle) {
        this.itemTitle = itemTitle;
        return this;
    }
    
    public List<Integer> getItemMediaTypeCode() {
        return itemMediaTypeCode;
    }
    public QuestionInsertVo setItemMediaTypeCode(List<Integer> itemMediaTypeCode) {
        this.itemMediaTypeCode = itemMediaTypeCode;
        return this;
    }
    
    public List<String> getItemMediaPath() {
        return itemMediaPath;
    }
    public QuestionInsertVo setItemMediaPath(List<String> itemMediaPath) {
        this.itemMediaPath = itemMediaPath;
        return this;
    }
    
    public List<String> getHashName() {
        return hashName;
    }
    public QuestionInsertVo setHashName(List<String> hashName) {
        this.hashName = hashName;
        return this;
    }   

    public MultipartFile getQuestionMediaFile(){
        return this.questionMediaFile;
    }
    
    public QuestionInsertVo setQuestionMediaFile(MultipartFile questionMediaFile){
        this.questionMediaFile = questionMediaFile;
        return this;
    }
    
    public List<MultipartFile> getItemMediaFiles(){
        return this.itemMediaFiles;
    }
    
    public QuestionInsertVo setItemMediaFiles(List<MultipartFile> itemMediaFiles){
        this.itemMediaFiles = itemMediaFiles;
        return this;
    }
     
    public List<ItemVo> getItemVoList() {
        return itemVoList;
    }
    

    public void setItemVoList(List<ItemVo> itemVoList) {
        this.itemVoList = itemVoList;
    }
    
    public boolean isMediaTypeImage(){
        return (this.mediaTypeCode == 1);        
    }
    
    public boolean isMediaPathEmpty(){
//        return this.mediaPath.length() == 0;
        return this.mediaPath != "";
    }
        
    public List<ItemVo> generateItemVo(){
        int itemTitleLength = this.itemTitle.size();
        int itemMediaPathLength = this.itemMediaPath.size();
        int itemMediaTypeCodeLength = this.itemMediaTypeCode.size();
        
        if((itemTitleLength != itemMediaPathLength) || (itemMediaPathLength != itemMediaTypeCodeLength)){
            return null;
        }
        
        int idx_item = 0;
        this.itemTitle.forEach(title -> {
            ItemVo itemVo = new ItemVo();
            itemVo.setTitle(title);
            itemVo.setMediaTypeCode(this.itemMediaTypeCode.get(idx_item));
            itemVo.setMediaPath(this.itemMediaPath.get(idx_item));
            itemVoList.add(itemVo);
        });
        return itemVoList;
    }
}
