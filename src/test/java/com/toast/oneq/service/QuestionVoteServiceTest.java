package com.toast.oneq.service;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.transaction.annotation.Transactional;

import com.toast.oneq.dao.HashDao;
import com.toast.oneq.dao.ItemDao;
import com.toast.oneq.dao.QuestionDao;
import com.toast.oneq.dao.ResultDao;
import com.toast.oneq.dao.UserDao;
import com.toast.oneq.vo.HashVo;
import com.toast.oneq.vo.ItemVo;
import com.toast.oneq.vo.PostCountVo;
import com.toast.oneq.vo.QuestionVo;
import com.toast.oneq.vo.ResultVo;
import com.toast.oneq.vo.UserVo;
import com.toast.oneq.vo.VoteVo;

@RunWith(MockitoJUnitRunner.class)
@ContextHierarchy ({
    @ContextConfiguration("/spring/application-context.xml"),
    @ContextConfiguration("/spring/servlet-context.xml")
})
@Transactional
@Rollback
public class QuestionVoteServiceTest {
    
    @Mock
    private QuestionDao questionDao;
    @Mock
    private ItemDao itemDao;
    @Mock
    private HashDao hashDao;
    @Mock
    private UserDao userDao;
    @Mock
    private ResultDao resultDao;
    
    @InjectMocks
    private QuestionVoteService questionVoteService;
    
    List<QuestionVo> questionList;
    QuestionVo question;
    List<ItemVo> itemList;
    ItemVo item;
    List<HashVo> hashList;
    HashVo hash;
    VoteVo vote;
    UserVo user;
    ResultVo result;
    List<ResultVo> resultList;
    
    PostCountVo postCountVo;
    
    int testQuestionId = 333;
    int testItemId = 222;
    int testHashId = 111;
    String testHashName = "테스트";
    int testUserId = 1;
    
    @Before
    public void setUp(){
        questionList = new ArrayList<QuestionVo>();
        question = new QuestionVo().setQuestionId(testQuestionId);
        questionList.add(question);
        
        itemList = new ArrayList<ItemVo>();
        item = new ItemVo().setItemId(testItemId);
        itemList.add(item);
        
        hashList = new ArrayList<HashVo>();
        hash = new HashVo().setHashId(testHashId);
        hashList.add(hash);
        
        vote = new VoteVo().setQuestionTypeCode(testQuestionId).setQuestionId(testUserId).setItemId(testItemId);
        user = new UserVo().setUserId(testUserId);
        
        postCountVo = new PostCountVo();
        postCountVo
            .setWriteCount(questionDao.selectQuestionCount(testUserId))
            .setVoteCount(questionDao.selectVoteCount(testUserId));
        
        result = new ResultVo().setItemId(testItemId);
        resultList = new ArrayList<ResultVo>();
        resultList.add(result);
        
        testItemId = 5;
        testQuestionId = 2;
        testUserId = 1;
        
        item.setItemId(testItemId);
        question.setQuestionId(testQuestionId);
        user.setUserId(testUserId);
        vote.setItemId(testItemId);
        vote.setQuestionId(testQuestionId);
        vote.setUserId(testUserId);
        vote.setQuestionTypeCode(0);
    }
    
    @Test
    public void postVoteResultTest_비정상() throws Exception{
        when(itemDao.selectOneItem(vote)).thenReturn(null);
        assertThat(questionVoteService.postVoteResult(vote),equalTo("error"));
    }
    
    @Test
    public void postVoteResultTest_이전투표값이없을때() throws Exception{
        vote.setQuestionTypeCode(0);
        when(itemDao.selectOneItem(vote)).thenReturn(item);
        when(questionDao.isValidQuestion(testQuestionId)).thenReturn(question);
        when(userDao.selectUser(testUserId)).thenReturn(user);
        when(resultDao.selectVotedResult(vote)).thenReturn(resultList);
        when(resultDao.deleteResult(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(1);
        
        assertThat(questionVoteService.postVoteResult(vote),equalTo("success"));
    }
    
    @Test
    public void postVoteResultTest_처음투표_투표조회수증가() throws Exception{
        when(itemDao.selectOneItem(vote)).thenReturn(item);
        when(questionDao.isValidQuestion(testQuestionId)).thenReturn(question);
        when(userDao.selectUser(testUserId)).thenReturn(user);
        
        assertThat(questionVoteService.postVoteResult(vote),equalTo("success"));
    }
    
    @Test
    public void postVoteResultTest_멀티투표이면서_처음투표가_아닐때() throws Exception{
        vote.setQuestionTypeCode(1);
        when(itemDao.selectOneItem(vote)).thenReturn(item);
        when(questionDao.isValidQuestion(testQuestionId)).thenReturn(question);
        when(userDao.selectUser(testUserId)).thenReturn(user);
        when(resultDao.selectVotedResult(vote)).thenReturn(resultList);
        
        assertThat(questionVoteService.postVoteResult(vote),equalTo("success"));
    }
    
    @Test
    public void postVoteResultTest_처음투표_아니고_싱글투표일때() throws Exception{
        when(itemDao.selectOneItem(vote)).thenReturn(item);
        when(questionDao.isValidQuestion(testQuestionId)).thenReturn(question);
        when(userDao.selectUser(testUserId)).thenReturn(user);
        when(resultDao.selectVotedResult(vote)).thenReturn(resultList);
        
        assertThat(questionVoteService.postVoteResult(vote),equalTo("success"));
    }
    
    @Test
    public void postVoteResultTest_정상() throws Exception{
        when(itemDao.selectOneItem(vote)).thenReturn(item);
        when(questionDao.isValidQuestion(testQuestionId)).thenReturn(question);
        when(userDao.selectUser(testUserId)).thenReturn(user);
        
        assertThat(questionVoteService.postVoteResult(vote),equalTo("success"));
    }
    
    @Test
    public void updateVoteResultTest_비정상() throws Exception{
        when(itemDao.selectOneItem(vote)).thenReturn(null);

        assertThat(questionVoteService.updateVoteResult(vote),equalTo("error"));
    }
    
    @Test
    public void updateVoteResultTest_정상() throws Exception{
        when(itemDao.selectOneItem(vote)).thenReturn(item);
        when(questionDao.isValidQuestion(testQuestionId)).thenReturn(question);
        when(userDao.selectUser(testUserId)).thenReturn(user);
        
        assertThat(questionVoteService.updateVoteResult(vote),equalTo("success"));
    }
    
    @Test
    public void updateVoteResultTest_투표취소() throws Exception{
        when(itemDao.selectOneItem(vote)).thenReturn(item);
        when(questionDao.isValidQuestion(testQuestionId)).thenReturn(question);
        when(userDao.selectUser(testUserId)).thenReturn(user);
        when(resultDao.selectVotedResult(vote)).thenReturn(resultList);
        
        assertThat(questionVoteService.updateVoteResult(vote),equalTo("success"));
    }
    
    @Test
    public void updateVoteResultTest_이전투표값이없을때() throws Exception{
        when(itemDao.selectOneItem(vote)).thenReturn(item);
        when(questionDao.isValidQuestion(testQuestionId)).thenReturn(question);
        when(userDao.selectUser(testUserId)).thenReturn(user);
        when(resultDao.selectVotedResult(vote)).thenReturn(resultList);
        when(resultDao.deleteResult(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(1);
        
        assertThat(questionVoteService.updateVoteResult(vote),equalTo("success"));
    }
    
    @Test
    public void isValidVoteTest_item이_유효하지않을때() throws Exception {
        when(itemDao.selectOneItem(vote)).thenReturn(null);
        
        assertThat(questionVoteService.isValidVote(vote),equalTo(false));
    }
    
    @Test
    public void isValidVoteTest_question이_유효하지않을때() throws Exception {
        when(itemDao.selectOneItem(vote)).thenReturn(item);
        when(questionDao.isValidQuestion(testQuestionId)).thenReturn(null);
        when(userDao.selectUser(testUserId)).thenReturn(null);
        
        assertThat(questionVoteService.isValidVote(vote),equalTo(false));
    }
    
    @Test
    public void isValidVoteTest_user가_유효하지않을때() throws Exception {
        when(itemDao.selectOneItem(vote)).thenReturn(item);
        when(questionDao.isValidQuestion(testQuestionId)).thenReturn(question);
        when(userDao.selectUser(testUserId)).thenReturn(null);
        
        assertThat(questionVoteService.isValidVote(vote),equalTo(false));
    }
    
    @Test
    public void isValidVoteTest_true일때() throws Exception {
        when(itemDao.selectOneItem(vote)).thenReturn(item);
        when(questionDao.isValidQuestion(testQuestionId)).thenReturn(question);
        when(userDao.selectUser(testUserId)).thenReturn(user);
        
        assertThat(questionVoteService.isValidVote(vote),equalTo(true));
    }
    
    @Test
    public void isValidItemIdTest_유효하지않을때() throws Exception{
        when(itemDao.selectOneItem(vote)).thenReturn(null);
        assertThat(questionVoteService.isValidItemId(vote),equalTo(false));
    }
    
    @Test
    public void isValidItemIdTest_유효할때() throws Exception{
        when(itemDao.selectOneItem(vote)).thenReturn(item);
        assertThat(questionVoteService.isValidItemId(vote),equalTo(true));
    }
    
    @Test
    public void isValidQuestionTest_유효하지않을때() throws Exception{
        when(questionDao.isValidQuestion(testQuestionId)).thenReturn(null);
        assertThat(questionVoteService.isValidQuestion(vote),equalTo(false));
    }
    
    @Test
    public void isValidQuestionTest_유효할때() throws Exception{
        vote = new VoteVo().setQuestionId(testQuestionId);
        when(questionDao.isValidQuestion(testQuestionId)).thenReturn(question);
        assertThat(questionVoteService.isValidQuestion(vote),equalTo(true));
    }
    
    @Test
    public void isValidUserTest_유효하지않을때() throws Exception{
        when(userDao.selectUser(testUserId)).thenReturn(null);
        assertThat(questionVoteService.isValidUser(testUserId),equalTo(false));
    }
    
    @Test
    public void isValidUserTest_유효할때() throws Exception{
        when(userDao.selectUser(testUserId)).thenReturn(user);
        assertThat(questionVoteService.isValidUser(testUserId),equalTo(true));
    }
    
    @Test
    public void isAlreadyVoteTest_투표안했을때() throws Exception{
        resultList = new ArrayList<ResultVo>();
        assertThat(questionVoteService.isAlreadyVote(resultList),equalTo(false));
    }
    
    @Test
    public void isAlreadyVoteTest_투표했을때() throws Exception{
        assertThat(questionVoteService.isAlreadyVote(resultList),equalTo(true));
    }
    
    @Test
    public void isLastOneVoteTest_마지막항목아닐때() throws Exception{
        resultList = new ArrayList<ResultVo>();
        assertThat(questionVoteService.isLastOneVote(resultList),equalTo(false));
    }
    
    @Test
    public void isLastOneVoteTest_마지막항목() throws Exception{
        assertThat(questionVoteService.isLastOneVote(resultList),equalTo(true));
    }
}
