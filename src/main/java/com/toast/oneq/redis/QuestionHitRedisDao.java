package com.toast.oneq.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

public class QuestionHitRedisDao {
    @Autowired
    private RedisTemplate<String, Integer> redisTemplate;
    
    private String env;
    
    private int writeBackDelay;
    
    public QuestionHitRedisDao(String env, String writeBackDelay) {
        this.env = env;
        this.writeBackDelay = Integer.parseInt(writeBackDelay);
    }
    
    public void insertHit(int questionId, int hit) {
        redisTemplate.opsForValue().set(generateHitKeyName(questionId), hit);
        insertWriteBackTime(questionId);
    }
    public int increaseHit(int questionId) {
        long hitNumber = redisTemplate.opsForValue().increment(generateHitKeyName(questionId), 1);
        return (int) hitNumber;
    }
    public boolean isTimeToWriteBack(int questionId) {
        int prevTime = redisTemplate.opsForValue().get(generateWriteBackTimeKeyName(questionId));
        int currentTIme = currentTimeSecond();
        int timeGapMinutes = (currentTIme - prevTime) / 60;
        
        return (timeGapMinutes > writeBackDelay);
    }
    public void insertWriteBackTime(int questionId) {
        redisTemplate.opsForValue().set(generateWriteBackTimeKeyName(questionId), currentTimeSecond());
    }
    public boolean hasKey(int questionId){
        return redisTemplate.hasKey(generateHitKeyName(questionId)); 
    }
    
    public int currentTimeSecond() {
        return (int) (System.currentTimeMillis() /1000); 
    }
    
    public String generateHitKeyName(int questionId) {
        return "questionId:"+env+":"+questionId+":hit:count";
    }
    
    public String generateWriteBackTimeKeyName(int questionId) {
        return "questionId:"+env+":"+questionId+":hit:writeBackTime";
    }
}
