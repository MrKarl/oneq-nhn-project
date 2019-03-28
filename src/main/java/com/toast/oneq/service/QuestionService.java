package com.toast.oneq.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.toast.oneq.dao.HashDao;
import com.toast.oneq.dao.ItemDao;
import com.toast.oneq.dao.QuestionDao;
import com.toast.oneq.redis.QuestionHitRedisDao;
import com.toast.oneq.vo.PostCountVo;
import com.toast.oneq.vo.QuestionVo;

@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
public class QuestionService {
    @Autowired
    private ItemDao itemDao;
    @Autowired
    private QuestionDao questionDao;
    @Autowired
    private HashDao hashDao;
    @Autowired
    private QuestionHitRedisDao questionHitRedisDao;

    public List<QuestionVo> getQuestions(int startIndex, int count, int userId) {
        List<QuestionVo> questions = questionDao.selectQuestions(startIndex, count);
        return setItemsAndHashs(questions, userId); // TODO : 코드리뷰
    }
    
    public List<QuestionVo> getQuestionsByHash(String hash_name, int startIndex, int count, int userId) {
        List<QuestionVo> questions = questionDao.selectQuestionsByHash(hash_name, startIndex, count);
        return setItemsAndHashs(questions, userId);
        
    }
    
    public List<QuestionVo> getQuestionsByUser(int userId, int startIndex, int count) {
        List<QuestionVo> questions = questionDao.selectQuestionsByUser(userId, startIndex, count);
        return setItemsAndHashs(questions, userId);
        
    }
    
    private List<QuestionVo> setItemsAndHashs(List<QuestionVo> questions, int userId){ // TODO : 코드리뷰
        questions.stream().forEach(question -> {
            question.setItems(itemDao.selectItemList(question.getQuestionId(), userId));
            question.setHashs(hashDao.getHashList(question.getQuestionId()));
        });
        
        return questions;
    }
    
    public List<QuestionVo> getVoteQuestionsByUser(int userId, int startIndex, int count) {
        List<QuestionVo> questions = questionDao.selectVoteQuestionsByUser(userId, startIndex, count);
        return setItemsAndHashs(questions, userId);

    }
    
    public QuestionVo getQuestion(int questionId, int userId) {
        QuestionVo question = questionDao.selectQuestion(questionId);
        question.setItems(itemDao.selectItemList(questionId, userId));
        question.setHashs(hashDao.getHashList(questionId));

        return question;
    }
    
    // For my page : countOfMyQuestion / countOfMyVote
    public PostCountVo getPostCount(int userId) {
        PostCountVo postCountVo = new PostCountVo()
                .setWriteCount(questionDao.selectQuestionCount(userId))
                .setVoteCount(questionDao.selectVoteCount(userId));
        
        return postCountVo;
    }
    
    public int updateQuestionHit(int questionId) {
        int hitNum=0;
        if (!questionHitRedisDao.hasKey(questionId)) {
            hitNum = questionDao.selectQuestionHit(questionId) + 1;
            questionHitRedisDao.insertHit(questionId, hitNum);
        } else {
            hitNum = questionHitRedisDao.increaseHit(questionId);
            
            if(questionHitRedisDao.isTimeToWriteBack(questionId)) {
                questionDao.updateQuestionHit(questionId, hitNum);
                questionHitRedisDao.insertWriteBackTime(questionId);
            }
        }
        
        return hitNum;
    }
}
