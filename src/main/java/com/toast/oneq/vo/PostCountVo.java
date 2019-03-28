package com.toast.oneq.vo;

public class PostCountVo { // TODO: 코드리뷰

    private int writeCount;
    private int voteCount;
    
    public int getWriteCount() {
        return writeCount;
    }
    public PostCountVo setWriteCount(int write) {
        this.writeCount = write;
        return this;
    }
    public int getVoteCount() {
        return voteCount;
    }
    public PostCountVo setVoteCount(int vote) {
        this.voteCount = vote;
        return this;
    }
    
}
