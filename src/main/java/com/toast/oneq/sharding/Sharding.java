package com.toast.oneq.sharding;

import org.mybatis.spring.SqlSessionTemplate;

public interface Sharding {

    public int getShardingIndex(int shardingKey);
    public int getShardingKey();
    public SqlSessionTemplate getShard(int shardingKey);
    public SqlSessionTemplate getAnyShard();
    
}
