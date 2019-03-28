package com.toast.oneq.dao;

import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.toast.oneq.vo.QuestionInsertVo;
import com.toast.oneq.vo.QuestionVo;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextHierarchy({
    @ContextConfiguration("/spring/application-context.xml"),
    @ContextConfiguration("/spring/servlet-context.xml")
})
@Transactional
@Rollback
public class QuestionDaoTest {
    @Autowired
    private QuestionDao questionDao;
    
    @Autowired
    private RedisTemplate<String, Integer> redisTemplate;
    
    @Test
    public void updateVoteNumUpTest(){
        assertThat(questionDao.updateVoteNumUp(1), equalTo(1));
        assertThat(questionDao.updateVoteNumUp(10), equalTo(1));
        assertThat(questionDao.updateVoteNumUp(100), equalTo(0));
    }
    
    @Test
    public void updateVoteNumDownTest(){
        assertThat(questionDao.updateVoteNumDown(1), equalTo(1));
        assertThat(questionDao.updateVoteNumDown(10), equalTo(1));
        assertThat(questionDao.updateVoteNumDown(100), equalTo(0));
    }
    
    @Test
    public void getQuestionsTest(){
        List<QuestionVo> questionList = questionDao.selectQuestions(0, 11);
        List<QuestionVo> Exceed_questionList = questionDao.selectQuestions(11, 5);
        assertThat(questionList.get(0).getQuestionId(), equalTo(10));
        assertThat(questionList.get(5).getQuestionId(), equalTo(1));
        assertThat(questionList.size(), equalTo(10));
        assertThat(Exceed_questionList.size(), equalTo(0));
    }
    
    @Test
    public void getQuestionsByHashTest() {
        List<QuestionVo> questionList = questionDao.selectQuestionsByHash("드림카", 0, 10);
        assertThat(questionList.get(0).getQuestionId(), equalTo(2));
        assertThat(questionList.size(), equalTo(2));
    }
    
    @Test
    public void getQuestionsByUserTest(){
        List<QuestionVo> questionList = questionDao.selectQuestionsByUser(10, 0, 10);
        List<QuestionVo> noneQuestionLIst = questionDao.selectQuestionsByUser(9, 0, 10);
        
        assertThat(questionList.get(0).getQuestionId(), equalTo(1));
        assertThat(questionList.size(), equalTo(5));
        assertThat(noneQuestionLIst.size(), equalTo(0));
    }
    
    @Test
    public void getVoteQuestionsByUserTest(){
        List<QuestionVo> questionList = questionDao.selectVoteQuestionsByUser(1, 0, 10);
        List<QuestionVo> noneQuestionList = questionDao.selectVoteQuestionsByUser(10, 0, 10);
        
        assertThat(questionList.get(0).getQuestionId(), equalTo(1));
        assertThat(noneQuestionList.size(), equalTo(0));
    }
    
    @Test
    public void getValidQuestionTest(){
        QuestionVo validQuestion = questionDao.isValidQuestion(1);
        QuestionVo timeOverQuestion = questionDao.isValidQuestion(3);
        QuestionVo timeUnderQuestion = questionDao.isValidQuestion(2);
        QuestionVo limitQuestion = questionDao.isValidQuestion(5);
        
        assertThat(validQuestion.getQuestionId(), equalTo(1));
        assertThat(timeOverQuestion, nullValue());
        assertThat(timeUnderQuestion, nullValue());
//        assertThat(limitQuestion, nullValue());
    }
    
    @Test
    public void getQuestionTest(){
        QuestionVo validQuestion = questionDao.selectQuestion(1);
        QuestionVo inValidQuestion = questionDao.selectQuestion(31);
        
        assertThat(validQuestion.getQuestionId(), equalTo(1));
        assertThat(inValidQuestion, nullValue());
    }
    
    @Test
    public void getQuestionCountTest(){
        int questionCountUser3 = questionDao.selectQuestionCount(3);
        int questionCountUser10 = questionDao.selectQuestionCount(10);
        int questionCountUserMinus1 = questionDao.selectQuestionCount(-1);
        
        assertThat(questionCountUser3, equalTo(2));
        assertThat(questionCountUser10, equalTo(5));
        assertThat(questionCountUserMinus1, equalTo(0));
    }
    
    @Test
    public void getVoteCountTest(){
        int voteCountUser1 = questionDao.selectVoteCount(1);
        int voteCountUser100 = questionDao.selectVoteCount(100);
        
        assertThat(voteCountUser1, equalTo(1));
        assertThat(voteCountUser100, equalTo(0));
    }
    
    @Test
    public void 질문등록하고확인하기() throws Exception{
        redisTemplate.opsForValue().set("questionId:test:LASTKEY", 11);
        QuestionInsertVo question1 = new QuestionInsertVo()
                                            .setUserId(3)
                                            .setTitle("test1");
        int questionId= questionDao.insertQuestion(question1);
        assertThat(questionDao.selectQuestion(questionId).getTitle(),equalTo("test1"));
    }
    

    @Test
    public void getQuestionInsertVoTest() {
        int testQuestionId = 3;
        QuestionInsertVo questionInsertVo = questionDao.selectQuestionInsertVo(testQuestionId);
        assertThat(questionInsertVo.getQuestionId(), equalTo(testQuestionId));
    }
    
    @Test
    public void selectQuestionHitTest() {
        assertThat(questionDao.selectQuestionHit(1), equalTo(0));
        assertThat(questionDao.updateQuestionHit(1, 3), equalTo(1));
        assertThat(questionDao.selectQuestionHit(1), equalTo(3));
    }
}
