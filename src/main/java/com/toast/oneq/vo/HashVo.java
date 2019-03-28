package com.toast.oneq.vo;

public class HashVo {
    private int hashId;
    private int hashCount;
    private String hashName;
    
    public int getHashId() {
        return hashId;
    }
    public HashVo setHashId(int hashId) {
        this.hashId = hashId;
        return this;
    }
    public String getHashName() {
        return hashName;
    }
    public HashVo setHashName(String hashName) {
        this.hashName = hashName;
        return this;
    }

    public int getHashCount() {
        return hashCount;
    }
    public HashVo setHashCount(int hashCount) {
        this.hashCount = hashCount;
        return this;
    }
}
