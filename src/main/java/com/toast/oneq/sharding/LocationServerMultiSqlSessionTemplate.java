package com.toast.oneq.sharding;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

public class LocationServerMultiSqlSessionTemplate extends MultiSqlSessionTemplate{
    @Autowired
    private RedisTemplate<String, Integer> redisTemplate;
    
    private String shardingKeyName;
    
    private String autoIncrementKeyName;
    
    public LocationServerMultiSqlSessionTemplate(String shardingKeyName) {
        this.shardingKeyName = shardingKeyName;
        this.autoIncrementKeyName = shardingKeyName+":LASTKEY";
    }
    
    @Override
    public int getShardingIndex(int shardingKey) {
        String serializedKey = serializeKey(shardingKey);
        int shardingIndex;
        if(redisTemplate.hasKey(serializedKey)) {
            shardingIndex = redisTemplate.opsForValue().get(serializedKey);
        } else {
            shardingIndex = hashFunction(shardingKey);
            redisTemplate.opsForValue().set(serializedKey, shardingIndex);
        }
        return shardingIndex;
    }

    @Override
    public int getShardingKey() {
        if(!redisTemplate.hasKey(autoIncrementKeyName)) {
            redisTemplate.opsForValue().set(autoIncrementKeyName, 0);
        }
        long lastKey = redisTemplate.opsForValue().increment(autoIncrementKeyName, 1); 
        return (int) lastKey;
    }
    
    private String serializeKey(int shardingKey) {
        return shardingKeyName+":"+shardingKey;
    }
    
    private int hashFunction(int shardingKey) {
        return shardingKey%super.getSizeOfSSTList();
    }
}
