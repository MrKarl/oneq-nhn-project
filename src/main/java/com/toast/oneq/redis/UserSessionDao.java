package com.toast.oneq.redis;


import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserSessionDao {
    @Autowired
    private RedisTemplate<String, Integer> redisTemplate;
    
    public void appendUserId(String uuid, int userId) {
        redisTemplate.opsForValue().set(uuid,  userId, 1, TimeUnit.DAYS);
    }
    
    public int getUserId(String uuid) {
        return redisTemplate.opsForValue().get(uuid);
    }
    public void deleteUuid(String uuid) {
        redisTemplate.delete(uuid);
    }
    public boolean isValidUuid(String uuid){
        return redisTemplate.hasKey(uuid); 
    }
}
