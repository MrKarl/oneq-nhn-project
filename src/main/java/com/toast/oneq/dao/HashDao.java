package com.toast.oneq.dao;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.toast.oneq.sharding.MultiSqlSessionTemplate;
import com.toast.oneq.vo.HashVo;

@Repository
public class HashDao {
    private static final String NAMESPACE = "hashDao.";
    
    @Autowired
    private MultiSqlSessionTemplate MultiqlSessionTemplate;
    
    public List<HashVo> getHashList(int questionId) {
        return MultiqlSessionTemplate.getShard(questionId).selectList(NAMESPACE + "selectHashList", questionId);
    }
    
    public int insertHashs(int questionId, List<HashVo> hashList){
        int insertListCount = 0;
        for(HashVo hvo : hashList) {
            insertHash(questionId, hvo);
            insertListCount++;
        }
        
        return insertListCount;
    }
    
    public int insertHash(int questionId, HashVo hashVo){
        HashVo hash = selectHashCount(hashVo);
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("questionId", questionId);

        if( hash != null){
            hashMap.put("hashId", hash.getHashId());
            MultiqlSessionTemplate.insert(NAMESPACE + "updateHashCount", hash.getHashId());
            MultiqlSessionTemplate.getShard(questionId).insert(NAMESPACE + "insertHashQuestion", hashMap);
            return 1;
        }else{
            MultiqlSessionTemplate.insert(NAMESPACE + "insertHash", hashVo);
            hashMap.put("hashId", hashVo.getHashId());
            MultiqlSessionTemplate.getShard(questionId).insert(NAMESPACE + "insertHashQuestion", hashMap);
            return 2;
        }
    }
    
    public HashVo selectHashCount(HashVo hvo){
        return MultiqlSessionTemplate.getAnyShard().selectOne(NAMESPACE + "selectHashCount", hvo);
    }
    
    public int updateHashCount(HashVo hvo){
        return MultiqlSessionTemplate.update(NAMESPACE + "updateHashCount", hvo.getHashId());
    }
}
