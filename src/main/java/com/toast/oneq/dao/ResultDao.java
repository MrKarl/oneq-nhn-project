package com.toast.oneq.dao;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.toast.oneq.sharding.MultiSqlSessionTemplate;
import com.toast.oneq.vo.ResultVo;
import com.toast.oneq.vo.VoteVo;

/**
 * Copyright 2016 NHN Entertainment Corp. All rights Reserved.
 * NHN Entertainment PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * tb_result에 접근하기 위한 DAO
 * @author ${email}
 */

@Repository
public class ResultDao {
    private static final String NAMESPACE = "resultDao.";
    
    @Autowired
    private MultiSqlSessionTemplate MultiSqlSessionTemplate;
    
	
    public List<ResultVo> selectVotedResult(VoteVo newVote){
        return MultiSqlSessionTemplate.getShard(newVote.getQuestionId()).selectList(NAMESPACE + "checkUserResult", newVote);
    }
    
	/**
	 * 항목(item)을 선택했을 때 호출되는 메소드
	 * @param 
	 * 	I_ID : 선택한 항목의 ID
	 * 	U_ID : 선택을 한 유저의 ID
	 * @return 
	 * 	
	 **/
	public int insertResult(VoteVo newVote) {
	    return MultiSqlSessionTemplate.getShard(newVote.getQuestionId()).insert(NAMESPACE + "insertResult", newVote);
	}
	
	public int updateResult(VoteVo newVote, ResultVo oldVote){
	    HashMap<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("newItemId", newVote.getItemId());
        resultMap.put("userId", newVote.getUserId());
        resultMap.put("oldItemId", oldVote.getItemId());        
        return MultiSqlSessionTemplate.getShard(newVote.getQuestionId()).update(NAMESPACE + "updateResult", resultMap);
	}
	
	public int deleteResult(int questionId, int itemId, int userId){
	    HashMap<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("itemId", itemId);
        resultMap.put("userId", userId);
	    return MultiSqlSessionTemplate.getShard(questionId).delete(NAMESPACE + "deleteResult", resultMap);
	}
}