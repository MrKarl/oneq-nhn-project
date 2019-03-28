package com.toast.oneq.dao;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.toast.oneq.sharding.MultiSqlSessionTemplate;
import com.toast.oneq.vo.ItemVo;
import com.toast.oneq.vo.VoteVo;

@Repository
public class ItemDao {
    private static final String NAMESPACE = "itemDao.";
    
    @Autowired
    private MultiSqlSessionTemplate MultiSqlSessionTemplate;
    
    public ItemVo selectOneItem(VoteVo newVote){
        return MultiSqlSessionTemplate.getShard(newVote.getQuestionId()).selectOne(NAMESPACE + "selectItem", newVote);
    }
    
    public int updateResultCountUp(int questionId, int itemId){
        return MultiSqlSessionTemplate.getShard(questionId).update(NAMESPACE + "updateCountUp", itemId);
    }
    
    public int updateResultCountDown(int questionId, int itemId){
        return MultiSqlSessionTemplate.getShard(questionId).update(NAMESPACE + "updateCountDown", itemId);
    }
    
    public List<ItemVo> selectItemList(int questionId){
        return MultiSqlSessionTemplate.getShard(questionId).selectList(NAMESPACE + "selectItemList", questionId);
    }
    
    public List<ItemVo> selectItemList(int questionId, int userId){
        HashMap<String, Integer> param = new HashMap<String,Integer>();
        param.put("questionId", questionId);
        param.put("userId", userId);
        return MultiSqlSessionTemplate.getShard(questionId).selectList(NAMESPACE + "selectItemListByUser", param);
    }

    public int insertItems(int questionId, List<ItemVo> itemList) throws NullPointerException {
        int insertListCount = 0;

        for(ItemVo itemVo : itemList) {
            insertItem(questionId, itemVo);
            insertListCount++;
        }
        return insertListCount;
    }

    public int insertItem(int questionId, ItemVo itemVo){
        itemVo.setQuestionId(questionId);
        return MultiSqlSessionTemplate.getShard(questionId).insert(NAMESPACE + "insertItem", itemVo);
   }

}