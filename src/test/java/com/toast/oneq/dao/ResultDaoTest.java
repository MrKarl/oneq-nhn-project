package com.toast.oneq.dao;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.toast.oneq.vo.ResultVo;
import com.toast.oneq.vo.VoteVo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextHierarchy ({
        @ContextConfiguration("/spring/application-context.xml"),
        @ContextConfiguration("/spring/servlet-context.xml")
})
@Transactional
@Rollback
public class ResultDaoTest {
    
    @Autowired
    ResultDao resultDao;
    
    // 1번 유저는 1번 설문에 1번 아이템을 투표한 dump data에 대해서 테스트
    static final int userId = 1;
    static final int votedItemId = 1;
    static final int updateItemId = 2;
    static final int votedQuestionId = 1;
    static final int newItemId =3;
    static final int newQuestionId = 2;
    VoteVo alreadyVote, newFirstVote, updateNewVote;
    ResultVo oldVote;
    
    @Before
    public void setUp(){
        // 이미 투표한 vote
        alreadyVote = new VoteVo().setUserId(userId)
                                  .setQuestionId(votedQuestionId)
                                  .setItemId(votedItemId);
        
        // 처음 참여하는 vote
        newFirstVote = new VoteVo().setUserId(userId)
                                   .setItemId(newItemId)
                                   .setQuestionId(newQuestionId);
        
        // 이미 참여한 투표에 항목을 변경하는 vote
        updateNewVote = new VoteVo().setUserId(userId)
                                 .setItemId(updateItemId)
                                 .setQuestionId(votedQuestionId);
        
        // 1번 설문에 투표한 result
        oldVote = new ResultVo().setUserId(userId)
                                .setItemId(votedItemId);
    }
    
    @Test
    public void getVotedResultTest(){
        List<ResultVo> resultList = resultDao.selectVotedResult(alreadyVote);
        assertThat(resultList.size(), equalTo(1));
        
        resultList = resultDao.selectVotedResult(newFirstVote);
        assertThat(resultList.size(), equalTo(0));
    }
    
    @Test(expected=DuplicateKeyException.class)
    public void postResultTest(){
        int statusInsertVoteResult = resultDao.insertResult(newFirstVote);
        assertThat(statusInsertVoteResult, equalTo(1));
        
        // 이미 넣어준 투표를 다시 넣으면 Duplicate exception
        statusInsertVoteResult = resultDao.insertResult(alreadyVote);
    }
    
    @Test
    public void updateResultTest(){
        int statusUpdateVoteResult = resultDao.updateResult(updateNewVote, oldVote);
        assertThat(statusUpdateVoteResult, equalTo(1));
        
        // oldVote와 newFirstVote는 서로 다른 질문
        statusUpdateVoteResult = resultDao.updateResult(newFirstVote, oldVote);
        assertThat(statusUpdateVoteResult, equalTo(0));
        
        // 이미 투표한 결과를 똑같은 결과로 업데이트 할때
        statusUpdateVoteResult = resultDao.updateResult(alreadyVote, oldVote);
        assertThat(statusUpdateVoteResult, equalTo(0));
    }
    
    @Test
    public void deleteResultTest(){
        int statusDeleteVoteResult = resultDao.deleteResult(alreadyVote.getQuestionId(),alreadyVote.getItemId(), alreadyVote.getUserId());
        assertThat(statusDeleteVoteResult, equalTo(1));
        
        statusDeleteVoteResult = resultDao.deleteResult(updateNewVote.getQuestionId(),updateNewVote.getItemId(), updateNewVote.getUserId());
        assertThat(statusDeleteVoteResult, equalTo(0));
    }
}
