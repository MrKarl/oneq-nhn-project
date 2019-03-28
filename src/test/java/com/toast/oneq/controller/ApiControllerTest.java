package com.toast.oneq.controller;

import static org.mockito.Matchers.notNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toast.oneq.exception.QuestionInsertFormException;
import com.toast.oneq.exception.ServiceFailException;
import com.toast.oneq.exception.UnauthorizedUserException;
import com.toast.oneq.redis.UserSessionDao;
import com.toast.oneq.service.QuestionInsertService;
import com.toast.oneq.service.QuestionService;
import com.toast.oneq.service.QuestionVoteService;
import com.toast.oneq.validator.QuestionInsertValidator;
import com.toast.oneq.vo.PostCountVo;
import com.toast.oneq.vo.QuestionInsertVo;
import com.toast.oneq.vo.QuestionVo;
import com.toast.oneq.vo.VoteVo;

@RunWith(MockitoJUnitRunner.class)
public class ApiControllerTest {
    @Mock
    private QuestionInsertService questionInsertService;
    
    @Mock
    private QuestionService questionService;
    
    @Mock
    private QuestionVoteService questionVoteService;
    
    @Mock
    private UserSessionDao userSessionDao;
    
    @Mock
    private QuestionInsertValidator questionInsertValidator;
    
    @InjectMocks
    private ApiController apiController;
    
    private MockMvc mockMvc;
    
    private static List<QuestionVo> questionList;
    private static int userId0 = 0;
    private static int userId1 = 1;
    
    @BeforeClass
    public static void beforeClass() {
        questionList = new ArrayList<QuestionVo>();
        questionList.add(new QuestionVo().setQuestionId(1).setTitle("판교맛집"));
        questionList.add(new QuestionVo().setQuestionId(2).setTitle("서울맛집"));
        questionList.add(new QuestionVo().setQuestionId(3).setTitle("분당맛집"));
    }
    
    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(apiController).build();
        Mockito.when(userSessionDao.isValidUuid("0")).thenReturn(true);
        Mockito.when(userSessionDao.getUserId("0")).thenReturn(0);
        Mockito.when(userSessionDao.isValidUuid("1")).thenReturn(true);
        Mockito.when(userSessionDao.getUserId("1")).thenReturn(1);
    }
    
    @Test
    public void saveQuestionTest_성공() throws Exception {
        Mockito.when(questionInsertService.saveQuestionAndItemAndHash((QuestionInsertVo)notNull()))
                .thenReturn(1);
        
        mockMvc.perform(post("/api/question")
                .requestAttr("userId", userId1))
                .andExpect(status().isOk());
    }
    
    @Test(expected = ServiceFailException.class)
    public void saveQuestionTest_익셉션발생() throws Exception {
        Mockito.when(questionInsertService.saveQuestionAndItemAndHash((QuestionInsertVo)notNull()))
                .thenThrow(new RuntimeException());
        
        mockMvc.perform(post("/api/question")
                .requestAttr("userId", userId1))
                .andExpect(status().isOk());
    }
    
    @Test(expected=UnauthorizedUserException.class) 
    public void saveQuestionTest_로그인하지않은유저가_투표등록할때() throws Exception {
        Mockito.when(userSessionDao.isValidUuid("0")).thenReturn(false);
        
        mockMvc.perform(post("/api/question")
                .requestAttr("userId", userId0))
                .andExpect(status().isOk());
    }
    
    @Test
    public void saveQuestionValidationTest_성공() throws Exception {
        QuestionInsertVo questionInsertVo = new QuestionInsertVo();
        
        BindingResult errors = new BeanPropertyBindingResult(questionInsertVo, "validForm");        
        apiController.saveQuestionValidation(questionInsertVo, errors);
    }

    @Test(expected=QuestionInsertFormException.class)
    public void saveQuestionValidationTest_폼익셉션() throws QuestionInsertFormException, JSONException {
        QuestionInsertVo questionInsertVo = new QuestionInsertVo();
        questionInsertVo.setTitle("");
        BindingResult errors = new BeanPropertyBindingResult(questionInsertVo, "inValidForm");
        
        Mockito.doAnswer(new Answer(){
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                BindingResult errors = (BindingResult) args[1];
                ObjectError error =  new ObjectError("QuestionInsertVo", "투표 제목을 입력하시지 않으셨어요.");
                errors.addError(error);
                return null;
            }
        }).when(questionInsertValidator).validate(questionInsertVo, errors);
        apiController.saveQuestionValidation(questionInsertVo, errors);
    }

    @Test
    public void getQuestionsTest_uuid가0일때() throws Exception {
        Mockito.when(questionService.getQuestions(0, 10, 0)).thenReturn(questionList);
        
        mockMvc.perform(get("/api/question")
                .requestAttr("userId", userId0))
                .andExpect(status().isOk());
    }
    
    @Test
    public void getQuestionsTest_uuid가있을때() throws Exception {
        Mockito.when(questionService.getQuestions(0, 10, 1)).thenReturn(questionList);

        mockMvc.perform(get("/api/question")
                .requestAttr("userId", userId1))
                .andExpect(status().isOk());
    }
    
    @Test
    public void getQuestionTest_uuid가0일때() throws Exception {
        QuestionVo questionVo = new QuestionVo().setQuestionId(1).setTitle("some title");
        Mockito.when(questionService.getQuestion(1, 0)).thenReturn(questionVo);
        
        mockMvc.perform(get("/api/question/1")
                .requestAttr("userId", userId0))
                .andExpect(status().isOk());
    }
    
    @Test
    public void getQuestionTest_uuid가있을때() throws Exception {
        QuestionVo questionVo = new QuestionVo().setQuestionId(1).setTitle("some title");
        Mockito.when(questionService.getQuestion(1, 1)).thenReturn(questionVo);
        
        mockMvc.perform(get("/api/question/1")
                .requestAttr("userId", userId1))
                .andExpect(status().isOk());
    }
    
    @Test
    public void getQuestionsByHashTest_uuid가0일때() throws Exception {
        Mockito.when(questionService.getQuestionsByHash("food",0, 10, 0)).thenReturn(questionList);
        
        mockMvc.perform(get("/api/tag/food")
                .requestAttr("userId", userId0))
                .andExpect(status().isOk());
    }
    
    @Test
    public void getQuestionsByHashTest_uuid가있을때() throws Exception {
        Mockito.when(questionService.getQuestionsByHash("food",0, 10, 1)).thenReturn(questionList);
        
        mockMvc.perform(get("/api/tag/food")
                .requestAttr("userId", userId1))
                .andExpect(status().isOk());
    }
    
    @Test
    public void voteQuestionTest_성공() throws Exception {
        VoteVo newVote = new VoteVo().setItemId(1).setUserId(1).setQuestionId(1).setQuestionTypeCode(1);
        Mockito.when(questionVoteService.postVoteResult((VoteVo)notNull())).thenReturn("success");
        this.voteQuestionTest(newVote, userId1);
    }
    
    @Test(expected = ServiceFailException.class)
    public void voteQuestionTest_실패() throws Exception {
        VoteVo newVote = new VoteVo().setItemId(1).setUserId(1).setQuestionId(1).setQuestionTypeCode(1);
        Mockito.when(questionVoteService.postVoteResult((VoteVo)notNull())).thenReturn("fail");
        this.voteQuestionTest(newVote,userId1);
    }
    
    @Test(expected=UnauthorizedUserException.class) 
    public void voteQuestionTest_로그인하지않은유저가_투표할때() throws Exception {
        VoteVo newVote = new VoteVo().setItemId(1).setUserId(1).setQuestionId(1).setQuestionTypeCode(1);
        this.voteQuestionTest(newVote,userId0);
    }

    private void voteQuestionTest(VoteVo newVote, int userId) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mockMvc.perform(post("/api/item").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(newVote))
                .requestAttr("userId", userId))
                .andExpect(status().isOk());
    }
    
    @Test
    public void updateQuestionTest_성공() throws Exception {
        VoteVo vote = new VoteVo().setItemId(1);
        Mockito.when(questionVoteService.updateVoteResult((VoteVo)notNull())).thenReturn("success");
        this.updateQuestionTest(vote,userId1);
    }
    
    @Test(expected = ServiceFailException.class)
    public void updateQuestionTest_실패() throws Exception {
        VoteVo vote = new VoteVo().setItemId(1);
        Mockito.when(questionVoteService.updateVoteResult((VoteVo)notNull())).thenReturn("fail");
        this.updateQuestionTest(vote,userId1);
    }
    
    @Test(expected = UnauthorizedUserException.class)
    public void updateQuestionTest_로그인하지않은사용자가_투표취소할때() throws Exception {
        VoteVo vote = new VoteVo().setItemId(1);
        this.updateQuestionTest(vote, userId0);
    }
    
    private void updateQuestionTest(VoteVo vote, int userId) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mockMvc.perform(put("/api/item").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(vote))
                .requestAttr("userId", userId))
                .andExpect(status().isOk());
        
    }
    
    @Test
    public void getPostCountTest_성공() throws Exception {
        PostCountVo postCountVo = new PostCountVo().setWriteCount(3).setVoteCount(3);
        Mockito.when(questionService.getPostCount(1)).thenReturn(postCountVo);
        
        mockMvc.perform(get("/api/my/question/post-count")
                .requestAttr("userId", userId0))
                .andExpect(status().isOk());
    }
    
    @Test
    public void getQuestionByUserTest_성공() throws Exception {
        Mockito.when(questionService.getQuestionsByUser(1, 0, 10)).thenReturn(questionList);
        
        mockMvc.perform(get("/api/my/question/write-question")
                .requestAttr("userId", userId0))
                .andExpect(status().isOk());
    }
    
    @Test
    public void getVoteQuestionsByUserTest_성공() throws Exception {
        Mockito.when(questionService.getVoteQuestionsByUser(1, 0, 10)).thenReturn(questionList);
        
        mockMvc.perform(get("/api/my/question/vote-question")
                .requestAttr("userId", userId0))
                .andExpect(status().isOk());
    }
}



