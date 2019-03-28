package com.toast.oneq.service;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.reflect.internal.WhiteboxImpl;

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
public class QuestionServiceTest {
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
    private QuestionService questionService;
    
    List<QuestionVo> questionList;
    QuestionVo question;
    List<ItemVo> itemList;
    ItemVo item;
    List<HashVo> hashList;
    HashVo hash;
    VoteVo vote;
    UserVo user;
    List<ResultVo> resultList;
    
    PostCountVo postCountVo;
    
    int testQuestionId = 333;
    int testItemId = 222;
    int testHashId = 111;
    String testHashName = "테스트";
    int testUserId = 1;
    
    
    int testStartIndex = 0;
    int testCount = 10;
    
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
        
        vote = new VoteVo().setQuestionTypeCode(testQuestionId).setUserId(testUserId).setItemId(testItemId);
        user = new UserVo().setUserId(testUserId);
    }
    
    @Test
    public void getQuestionsTest(){
        when(questionDao.selectQuestions(testStartIndex, testCount)).thenReturn(questionList);
        when(itemDao.selectItemList(testQuestionId)).thenReturn(itemList);
        when(hashDao.getHashList(testQuestionId)).thenReturn(hashList);
        
        assertThat(questionService.getQuestions(testStartIndex, testCount, testUserId).get(0),equalTo(question));
    }
    
    @Test
    public void getQuestionsByHashTest(){
        when(questionDao.selectQuestionsByHash(testHashName,testStartIndex, testCount)).thenReturn(questionList);
        when(itemDao.selectItemList(testQuestionId, testUserId)).thenReturn(itemList);
        when(hashDao.getHashList(testQuestionId)).thenReturn(hashList);
        
        assertThat(questionService.getQuestionsByHash(testHashName, testStartIndex, testCount, testUserId).get(0),equalTo(question));
    }
    
    @Test
    public void getQuestionsByUserTest(){
        when(questionDao.selectQuestionsByUser(testUserId, testStartIndex, testCount)).thenReturn(questionList);
        when(itemDao.selectItemList(testQuestionId, testUserId)).thenReturn(itemList);
        when(hashDao.getHashList(testQuestionId)).thenReturn(hashList);
        
        assertThat(questionService.getQuestionsByUser(testUserId, testStartIndex, testCount),equalTo(questionList));
    }
    
    @Test
    public void getVoteQuestionsByUserTest(){
        when(questionDao.selectVoteQuestionsByUser(testUserId, testStartIndex, testCount)).thenReturn(questionList);
        when(itemDao.selectItemList(testQuestionId, testUserId)).thenReturn(itemList);
        when(hashDao.getHashList(testQuestionId)).thenReturn(hashList);
        
        assertThat(questionService.getVoteQuestionsByUser(testUserId, testStartIndex, testCount),equalTo(questionList));
    }
    
    @Test
    public void getQuestionTest(){
        when(questionDao.selectQuestion(testQuestionId)).thenReturn(question);
        when(itemDao.selectItemList(testQuestionId, testUserId)).thenReturn(itemList);
        when(hashDao.getHashList(testQuestionId)).thenReturn(hashList);
        
        assertThat(questionService.getQuestion(testQuestionId,testUserId),equalTo(question));
    }
    
    @Test
    public void getPostCountTest() throws Exception{
        int questionCount = questionDao.selectQuestionCount(testUserId);
        int voteCount = questionDao.selectVoteCount(testUserId);
        PostCountVo postCountVo = new PostCountVo();
        postCountVo.setWriteCount(questionCount)
                   .setVoteCount(voteCount);

        when(questionDao.selectQuestionCount(testUserId)).thenReturn(questionCount);
        when(questionDao.selectVoteCount(testUserId)).thenReturn(voteCount);
        
        PostCountVo resultPostCountVo = WhiteboxImpl.invokeMethod(questionService, "getPostCount", testUserId);
        assertEquals(postCountVo.getVoteCount(), resultPostCountVo.getVoteCount());
        assertEquals(postCountVo.getWriteCount(), resultPostCountVo.getWriteCount());
    }
}
