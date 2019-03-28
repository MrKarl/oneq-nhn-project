package com.toast.oneq.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.toast.oneq.exception.QuestionInsertFormException;
import com.toast.oneq.exception.ServiceFailException;
import com.toast.oneq.exception.UnauthorizedUserException;
import com.toast.oneq.service.QuestionInsertService;
import com.toast.oneq.service.QuestionService;
import com.toast.oneq.service.QuestionVoteService;
import com.toast.oneq.util.ResponseHeaderUtil;
import com.toast.oneq.validator.QuestionInsertValidator;
import com.toast.oneq.vo.QuestionInsertVo;
import com.toast.oneq.vo.ResponseVo;
import com.toast.oneq.vo.VoteVo;

@RestController
@RequestMapping("/api")
public class ApiController {
    @Autowired
    private QuestionService questionService; // TODO: 코드리뷰
    @Autowired
    private QuestionInsertService questionInsertService;
    @Autowired
    private QuestionVoteService questionVoteService;
    @Autowired
    private QuestionInsertValidator questionInsertValidator;
    
    private static ResponseVo SUCCESS_RESPONSE = new ResponseVo().setHeader(ResponseHeaderUtil.RESPONSE_SUCCESS_HEADER);

    @RequestMapping(value="/question", method = RequestMethod.POST) 
    public ResponseVo saveQuestion(
            HttpServletRequest request,
            HttpServletResponse response,
            QuestionInsertVo questionInsertVo, BindingResult result) throws UnauthorizedUserException, ServiceFailException, QuestionInsertFormException, JSONException{
        int userId = (int) request.getAttribute("userId");
        if(userId == 0){
            throw new UnauthorizedUserException();
        }
        
        questionInsertVo.setUserId(userId);        
        saveQuestionValidation(questionInsertVo, result);
        
        try{
            int questionId = questionInsertService.saveQuestionAndItemAndHash(questionInsertVo);
            Map<String, Integer> questionIdMap = new HashMap<String, Integer>();
            questionIdMap.put("questionId", questionId);
            return SUCCESS_RESPONSE.setBody(questionIdMap);
        } catch(Exception e){
            throw new ServiceFailException(e.toString(), "투표 등록이 실패하였습니다.");
        }
    }
    protected void saveQuestionValidation(QuestionInsertVo questionInsertVo, BindingResult result) throws QuestionInsertFormException, JSONException{
        questionInsertValidator.validate(questionInsertVo, result);
        if(result.hasErrors()) {
            List<FieldError> errs = result.getFieldErrors();
            JSONObject json = new JSONObject();
            
            for(FieldError error : errs){
                json.put(error.getField(), error.getCode());
            }
            
            throw new QuestionInsertFormException(json.toString());
        }
    }
    
    @RequestMapping(value = "/question", method = RequestMethod.GET)
    public ResponseVo getQuestions(
            HttpServletRequest request,
            HttpServletResponse response, 
            @RequestParam(value="startIndex", defaultValue="0") int startIndex,
            @RequestParam(value="count", defaultValue="10") int count) throws UnauthorizedUserException {
        int userId = (int) request.getAttribute("userId");
        return SUCCESS_RESPONSE.setBody(questionService.getQuestions(startIndex, count, userId));
    }
    
    @RequestMapping(value = "/question/{questionId}", method = RequestMethod.GET)
    public ResponseVo getQuestion(
            HttpServletRequest request,
            HttpServletResponse response, 
            @PathVariable("questionId") int questionId) throws UnauthorizedUserException, ServiceFailException {
        int userId = (int) request.getAttribute("userId");
        try{
            return SUCCESS_RESPONSE.setBody(questionService.getQuestion(questionId, userId));
        } catch(Exception e) {
            throw new ServiceFailException(e.toString(), "투표를 가져오는데 실패했습니다.");
        }
    }
    
    @RequestMapping(value= "/question/{questionId}/hit", method = RequestMethod.POST)
    public ResponseVo hitQuestion(
            HttpServletRequest request,
            HttpServletResponse response, 
            @PathVariable("questionId") int questionId){
        int hitNum = questionService.updateQuestionHit(questionId);
        Map<String,Integer> hitMap = new HashMap<String,Integer>();
        hitMap.put("hit", hitNum);
        return SUCCESS_RESPONSE.setBody(hitMap);
    }

    @RequestMapping(value = "/tag/{hashName}", method = RequestMethod.GET)
    public ResponseVo getQuestionsByHash(
            HttpServletRequest request,
            HttpServletResponse response, 
            @PathVariable("hashName") String hash_name,
            @RequestParam(value="startIndex", defaultValue="0") int startIndex,
            @RequestParam(value="count", defaultValue="10") int count) throws UnauthorizedUserException, ServiceFailException {
        int userId = (int) request.getAttribute("userId");
        return SUCCESS_RESPONSE.setBody(questionService.getQuestionsByHash(hash_name, startIndex, count, userId));
    }
    
    @RequestMapping(value="/item", method=RequestMethod.POST)
    public ResponseVo voteQuestion(
            HttpServletRequest request,
            HttpServletResponse response, 
            @RequestBody VoteVo newVote) throws UnauthorizedUserException, ServiceFailException {
        int userId = (int) request.getAttribute("userId");
        if(userId == 0){
            throw new UnauthorizedUserException();
        }
        
        newVote.setUserId(userId);
        String state = questionVoteService.postVoteResult(newVote);
        if(state.equals("success")){// TODO: 코드리뷰
            return SUCCESS_RESPONSE.setBody(newVote);
        } else {
            throw new ServiceFailException("투표에 실패하였습니다.");
        }
    }
    
    @RequestMapping(value="/item", method=RequestMethod.PUT)
    public ResponseVo updateQuestion(
            HttpServletRequest request,
            HttpServletResponse response, 
            @RequestBody VoteVo newVote) throws UnauthorizedUserException, ServiceFailException {
        int userId = (int) request.getAttribute("userId");
        if(userId == 0){
            throw new UnauthorizedUserException();
        }
        newVote.setUserId(userId);
        String state = questionVoteService.updateVoteResult(newVote);
        if(state.equals("success")){
            return SUCCESS_RESPONSE.setBody(newVote);
        } else {
            throw new ServiceFailException("투표에 실패하였습니다.");
        }
    }
    
    @RequestMapping(value="/my/question/post-count", method = RequestMethod.GET)
    public ResponseVo getPostCount(
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        int userId = (int) request.getAttribute("userId");
        return SUCCESS_RESPONSE.setBody(questionService.getPostCount(userId));
    }
    
    @RequestMapping(value="/my/question/write-question", method = RequestMethod.GET)
    public ResponseVo getQuestionsByUser(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(value="startIndex", defaultValue="0") int startIndex,
            @RequestParam(value="count", defaultValue="10") int count) throws UnauthorizedUserException {
        int userId = (int) request.getAttribute("userId");
        return SUCCESS_RESPONSE.setBody(questionService.getQuestionsByUser(userId, startIndex, count));
    }
    
    @RequestMapping(value="/my/question/vote-question", method = RequestMethod.GET)
    public ResponseVo getVoteQuestionsByUser(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(value="startIndex", defaultValue="0") int startIndex,
            @RequestParam(value="count", defaultValue="10") int count) throws UnauthorizedUserException {
        int userId = (int) request.getAttribute("userId");
        return SUCCESS_RESPONSE.setBody(questionService.getVoteQuestionsByUser(userId, startIndex, count));
    }
}

