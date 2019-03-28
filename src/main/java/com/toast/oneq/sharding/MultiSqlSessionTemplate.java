package com.toast.oneq.sharding;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import com.toast.oneq.log.LogMaker;

public abstract class MultiSqlSessionTemplate implements Sharding {
    private List<SqlSessionTemplate> sqlSessionTemplateList;
    private int resultOfQuery;
    
    public abstract int getShardingIndex(int shardingKey);

    public abstract int getShardingKey();
    
    public SqlSessionTemplate getShard(int shardingKey){
        return sqlSessionTemplateList.get(getShardingIndex(shardingKey));
    }
    
    public SqlSessionTemplate getAnyShard(){
        int sizeOfSSTList = sqlSessionTemplateList.size();
        for(int i=0; i<sizeOfSSTList; i++){
            if(isAliveConnection(sqlSessionTemplateList.get(i))){
                return sqlSessionTemplateList.get(i);
            }
        }
        return sqlSessionTemplateList.get(1);
    }
    
    public void setSqlSessionTemplateList(List<SqlSessionTemplate> sqlSessionTemplateList) {
        this.sqlSessionTemplateList = sqlSessionTemplateList;
    }
    public List<SqlSessionTemplate> getSqlSessionTemplateList(){
        return this.sqlSessionTemplateList;
    }
    /*
     * For Duplicate Insert
     */
    @Transactional
    public int insert(String statement, Object parameter) {
        resultOfQuery = 0;
        sqlSessionTemplateList.stream().forEach(sqlSessionTemplate -> {
            resultOfQuery = sqlSessionTemplate.insert(statement, parameter);
        });
        return resultOfQuery;
    }
    /*
     * For Duplicate Update
     */
    @Transactional
    public int update(String statement, Object parameter) {
        resultOfQuery = 0;
        sqlSessionTemplateList.stream().forEach(sqlSessionTemplate -> {
            resultOfQuery = sqlSessionTemplate.update(statement, parameter);
        });
        return resultOfQuery;
    }
    public <E> List<E> selectLimitList(String statement, Map<String, Object> param) {
        int sizeOfSSTList = sqlSessionTemplateList.size();
        int startIndex = (int) param.get("startIndex");
        int count = (int) param.get("count");
        int startIndexOfFirst = startIndex/sizeOfSSTList + startIndex%sizeOfSSTList;
        int countOfFirst = count/sizeOfSSTList + count%sizeOfSSTList;
        int startIndexOfOthers = startIndex/sizeOfSSTList;
        int countOfOthers = count/sizeOfSSTList;
        List<Object> mergedList = new ArrayList<Object>();
        //First Element
        param.replace("startIndex", startIndexOfFirst);
        param.replace("count", countOfFirst);
        if(isAliveConnection(sqlSessionTemplateList.get(0))){
            mergedList.addAll(sqlSessionTemplateList.get(0).selectList(statement,param));
        }
        //Other Elements
        param.replace("startIndex", startIndexOfOthers);
        param.replace("count", countOfOthers);
        for(int i=1; i<sizeOfSSTList; i++){
            if(isAliveConnection(sqlSessionTemplateList.get(i))){
                mergedList.addAll(sqlSessionTemplateList.get(i).selectList(statement,param));
            }
        }
        return (List<E>) mergedList;
    }
    
    public int selectOneForSum(String statement, int parameter){
        int sizeOfSSTList = sqlSessionTemplateList.size();
        resultOfQuery=0;
        for(int i=0; i<sizeOfSSTList; i++){
            if(isAliveConnection(sqlSessionTemplateList.get(i))){
                resultOfQuery += (int) sqlSessionTemplateList.get(i).selectOne(statement,parameter);
            }
        }
        return resultOfQuery;
    }
    
    public int getSizeOfSSTList(){
        return sqlSessionTemplateList.size();
    }
    
    public boolean isAliveConnection(SqlSessionTemplate sqlSessionTemplate){
        try{
            sqlSessionTemplate.getConnection();
            return true;
        } catch(DataAccessException DAE) {
            LogMaker.error(DAE.getMessage());
            return false;
        }
    }
}
