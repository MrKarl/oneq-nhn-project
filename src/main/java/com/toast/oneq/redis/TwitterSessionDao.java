package com.toast.oneq.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TwitterSessionDao {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    public void appendRequestToken(String sessionId, String requestToken, String requestTokenSecret) {
        redisTemplate.opsForHash().put(sessionId, "requestToken", requestToken);
        redisTemplate.opsForHash().put(sessionId, "requestTokenSecret", requestTokenSecret);
    }
    
    public String getToken(String sessionId) {
        return (String)redisTemplate.opsForHash().get(sessionId, "requestToken");
    }
    
    public String getTokenSecret(String sessionId) {
        return (String)redisTemplate.opsForHash().get(sessionId, "requestTokenSecret");
    }
    
    public void deleteSessionId(String sessionId) {
        redisTemplate.opsForHash().delete(sessionId, "requestToken");
        redisTemplate.opsForHash().delete(sessionId, "requestTokenSecret");
    }
}