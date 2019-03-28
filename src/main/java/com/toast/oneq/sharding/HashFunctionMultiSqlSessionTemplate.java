package com.toast.oneq.sharding;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

public class HashFunctionMultiSqlSessionTemplate extends MultiSqlSessionTemplate{
    @Autowired
    private RedisTemplate<String, Integer> redisTemplate;
    
    private String shardingKeyName;
    
    private String autoIncrementKeyName;
    
    public HashFunctionMultiSqlSessionTemplate(String shardingKeyName) {
        this.shardingKeyName = shardingKeyName;
        this.autoIncrementKeyName = shardingKeyName+":LASTKEY";
    }
    
    @Override
    public int getShardingIndex(int shardingKey) {
        return this.hashFunction(shardingKey);
    }

    @Override
    public int getShardingKey() {
        if(!redisTemplate.hasKey(autoIncrementKeyName)) {
            redisTemplate.opsForValue().set(autoIncrementKeyName, 0);
        }
        long lastKey = redisTemplate.opsForValue().increment(autoIncrementKeyName, 1); 
        return (int) lastKey;
    }
        
    private int hashFunction(int shardingKey) {
        return shardingKey%super.getSizeOfSSTList();
    }
}
