package com.toast.oneq.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toast.oneq.dao.ItemDao;
import com.toast.oneq.dao.QuestionDao;
import com.toast.oneq.dao.ResultDao;
import com.toast.oneq.dao.UserDao;
import com.toast.oneq.vo.ResultVo;
import com.toast.oneq.vo.VoteVo;

@Service
public class QuestionVoteService {
    @Autowired
    private ResultDao resultDao;
    @Autowired
    private ItemDao itemDao;
    @Autowired
    private QuestionDao questionDao;
    @Autowired
    private UserDao userDao;

    public String postVoteResult(VoteVo newVote) {
        if (!isValidVote(newVote)) {
            return "error";
        }

        List<ResultVo> oldVotes = resultDao.selectVotedResult(newVote);
        
        if (!isAlreadyVote(oldVotes)) {
            questionDao.updateVoteNumUp(newVote.getQuestionId());
        } else if(newVote.isSingleVoteType()){
            ResultVo oldVote = oldVotes.get(0);
            int isSuccessToDelete = resultDao.deleteResult(newVote.getQuestionId(),oldVote.getItemId(), oldVote.getUserId());
            if ( isSuccessToDelete != 0 ) {
                itemDao.updateResultCountDown(newVote.getQuestionId(),oldVote.getItemId());
            }
        }
        
        resultDao.insertResult(newVote);
        itemDao.updateResultCountUp(newVote.getQuestionId(),newVote.getItemId());

        return "success";
    }

    public String updateVoteResult(VoteVo newVote) {
        if (!isValidVote(newVote)) {
            return "error";
        }

        List<ResultVo> oldVotes = resultDao.selectVotedResult(newVote);
        int isSuccessToDelete = resultDao.deleteResult(newVote.getQuestionId(),newVote.getItemId(), newVote.getUserId());
        if ( isSuccessToDelete != 0 ) {
            itemDao.updateResultCountDown(newVote.getQuestionId(),newVote.getItemId());
        }

        if (isLastOneVote(oldVotes)) {
            questionDao.updateVoteNumDown(newVote.getQuestionId());
        }

        return "success";
    }

    // valid check valid item, question, user
    protected boolean isValidVote(VoteVo newVote) {
        return isValidItemId(newVote) && isValidQuestion(newVote) && isValidUser(newVote.getUserId());
    }

    protected boolean isValidItemId(VoteVo newVote) {
        return itemDao.selectOneItem(newVote) != null;
    }

    // Check able to vote question (end_time, vote num limit check)
    protected boolean isValidQuestion(VoteVo newVote) {
        return questionDao.isValidQuestion(newVote.getQuestionId())!= null;
    }

    protected boolean isValidUser(int userId) {
        return userDao.selectUser(userId) != null;
    }

    protected boolean isAlreadyVote(List<ResultVo> oldVote) {
        return oldVote.size() != 0;
    }

    // Check Last One Vote
    protected boolean isLastOneVote(List<ResultVo> oldVote) {
        return oldVote.size() == 1;
    }
}
