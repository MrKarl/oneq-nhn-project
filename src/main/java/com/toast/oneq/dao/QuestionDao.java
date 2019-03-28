package com.toast.oneq.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.toast.oneq.sharding.MultiSqlSessionTemplate;
import com.toast.oneq.vo.QuestionInsertVo;
import com.toast.oneq.vo.QuestionVo;

/**
 * Copyright 2016 NHN Entertainment Corp. All rights Reserved.
 * NHN Entertainment PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * tb_question에 접근할 때 사용하는 DAO
 * @author ${email}
 */

@Repository
public class QuestionDao {
    private static final String NAMESPACE = "questionDao.";
    
    @Autowired
    private MultiSqlSessionTemplate multiSqlSessionTemplate;
    
    public int updateVoteNumUp(int questionId){
        return multiSqlSessionTemplate.getShard(questionId).update(NAMESPACE + "updateVoteNumUp", questionId);

    }
    
    public int updateVoteNumDown(int questionId){
        return multiSqlSessionTemplate.getShard(questionId).update(NAMESPACE + "updateVoteNumDown", questionId);
    }
    
    /**
	 * 질문의 list를 불러오기 위한 메소드
	 * @param
	 * 	startIndex : 어디서 붙어 가져올지에 대한 index 값
	 * 	endIndex : 어디 까지 가져올지에 대한 index 값
	 * @return
	 * 	정렬된 question중에서 startIndex, endIndex 사이에 있는 값의 ArrayList
	 **/
	public List<QuestionVo> selectQuestions(int startIndex, int count) {
	    Map<String, Object> param = new HashMap<String, Object>();
	    param.put("startIndex", startIndex);
	    param.put("count", count);
		return multiSqlSessionTemplate.selectLimitList(NAMESPACE + "getQuestions", param);
	}
	
	public List<QuestionVo> selectQuestionsByHash(String hashName, int startIndex, int count) {
	    
	    Map<String, Object> param = new HashMap<String, Object>();
	    param.put("hashName", hashName);
	    param.put("startIndex", startIndex);
	    param.put("count", count);
	    
	    return multiSqlSessionTemplate.selectLimitList(NAMESPACE + "getQuestionsByHash", param);
	    
	}
	/**
	 * 유저가 등록한 질문 list를 불러오는 메소드
	 * @param userId
	 * @param startIndex
	 * @param count
	 * @return
	 *     정렬된 question중에서 startIndex, endIndex 사이에 있는 값의 ArrayList
	 */
	public List<QuestionVo> selectQuestionsByUser(int userId, int startIndex, int count) {
	    
	    Map<String, Object> param = new HashMap<String, Object>();
	    param.put("userId", userId);
	    param.put("startIndex", startIndex);
	    param.put("count", count);
	    
	    return multiSqlSessionTemplate.selectLimitList(NAMESPACE + "getQuestionsByUser", param);
	    
	}
	/**
	 * 유저가 투표 참여한 질문 list를 불러오는 메소드
	 * @param userId
	 * @param startIndex
	 * @param count
	 * @return
	 *    정렬된 question중에서 startIndex, endIndex 사이에 있는 값의 ArrayList
	 */ 
    public List<QuestionVo> selectVoteQuestionsByUser(int userId, int startIndex, int count) {

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userId", userId);
        param.put("startIndex", startIndex);
        param.put("count", count);

        return multiSqlSessionTemplate.selectLimitList(NAMESPACE + "getVoteQuestionsByUser", param);

    }
    
	public QuestionVo isValidQuestion(int questionId){
	    return multiSqlSessionTemplate.getShard(questionId).selectOne(NAMESPACE + "selectValidQuestion", questionId);
	}
	/**
	 * 하나의 질문만을 가져오기 위한 메소드
	 * @param 
	 * 	Q_ID : 가져오려는 징문의 Q_ID
	 * @return 
	 * 	Q_ID의 값에 해당하는 징문으로 정보를 넣은 QuestionVO
	 **/
	public QuestionVo selectQuestion(int questionId) {
	    return multiSqlSessionTemplate.getShard(questionId).selectOne(NAMESPACE + "getQuestion", questionId);
	}
	

	/**
	 * 유저가 작성한 질문의 개수를 가져오기 위한 메소드
	 * @param 
	 * 	U_ID : 질문의 개수를 가져오려는 유저 아이디
	 * @return 
	 * 	U_ID에 해당하는 유저가 작성한 질문 개수
	 **/
	public int selectQuestionCount(int userId) {
		return multiSqlSessionTemplate.selectOneForSum(NAMESPACE + "getQuestionCount", userId);
	}
	

	/**
	 * 유저가 투표한 질문의 개수를 가져오기 위한 메소드
	 * @param
	 * 	U_ID : 투표한 질문의 개수를 가져오려는 유저 아이디
	 * @return
	 * 	U_ID에 해당하는 유저가 투표한 질문 개수
	 **/
	public int selectVoteCount(int userId) {
		return multiSqlSessionTemplate.selectOneForSum(NAMESPACE + "getVoteCount", userId);
	}
	
	/**
	 * 새로운 투표 등록 메소드
	 * @param QuestionInsertVo
	 * @return
	 *     QuestionId
	 */
	public int insertQuestion(QuestionInsertVo questionInsertVo) {
	    int questionId = multiSqlSessionTemplate.getShardingKey();
	    questionInsertVo.setQuestionId(questionId);
        multiSqlSessionTemplate.getShard(questionId).insert(NAMESPACE+"insertQuestion", questionInsertVo);
        return questionId;
    }
	
	public QuestionInsertVo selectQuestionInsertVo(int questionId) {
	    QuestionInsertVo questionInsertVo = multiSqlSessionTemplate.getShard(questionId).selectOne(NAMESPACE + "getQuestionInsert", questionId);
	    return questionInsertVo;
	}
	
	public int selectQuestionHit(int questionId) {
	    return multiSqlSessionTemplate.getShard(questionId).selectOne(NAMESPACE + "selectQuestionHit", questionId);
	}
	
	public int updateQuestionHit(int questionId, int hit) {
	    Map<String, Integer> questionHitMap = new HashMap<String,Integer>();
	    questionHitMap.put("questionId", questionId);
	    questionHitMap.put("hit", hit);
	    return multiSqlSessionTemplate.getShard(questionId).update(NAMESPACE + "updateQuestionHit", questionHitMap);
	}
}