package com.toast.oneq.dao;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.toast.oneq.vo.ItemVo;
import com.toast.oneq.vo.VoteVo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextHierarchy ({
        @ContextConfiguration("/spring/application-context.xml"),
        @ContextConfiguration("/spring/servlet-context.xml")
})
@Transactional
@Rollback
public class ItemDaoTest {

    @Autowired
    private ItemDao itemDao;
    
    private static final int testUserId = 1;
    private static final int testQuestionId = 1;
    private static final int testItemId = 1;
    private static final String testTitle = "스위스";
    private static final int testMediaType = 1;
    
    private ItemVo itemVo;
    private VoteVo voteVo;
    
    @Before
    public void setup() {
        voteVo = new VoteVo()
                .setItemId(testItemId)
                .setQuestionId(testQuestionId);
        
        itemVo = itemDao.selectOneItem(voteVo);
    }
    @Test
    public void getOneItemTest() {
        assertThat(itemVo.getItemId(), equalTo(testItemId));
        assertThat(itemVo.getQuestionId(), equalTo(testQuestionId));
        assertThat(itemVo.getTitle(), equalTo(testTitle));
        assertThat(itemVo.getMediaTypeCode(), equalTo(testMediaType));
        assertThat(itemVo.getResultCount(), equalTo(0));
    }
    
    @Test
    public void updateResultCountUpTest() {
        int preResultCount = itemVo.getResultCount();
        assertThat(itemDao.updateResultCountUp(testQuestionId,testItemId), equalTo(1));
        assertThat(itemDao.selectOneItem(voteVo).getResultCount(), equalTo(preResultCount+1));
    }
    
    @Test
    public void updateResultCountDownTest() {
        int preResultCount = itemVo.getResultCount();
        assertThat(itemDao.updateResultCountDown(testQuestionId,testItemId), equalTo(1));
        assertThat(itemDao.selectOneItem(voteVo).getResultCount(), equalTo(preResultCount-1));
    }
    
    @Test
    public void getItemListByQuestionIdTest() {
        List<ItemVo> itemVos = itemDao.selectItemList(testQuestionId);
        assertThat(itemVos.get(0).getItemId(), equalTo(1));
        assertThat(itemVos.get(1).getItemId(), equalTo(2));
        assertThat(itemVos.get(2).getItemId(), equalTo(3));
    }
    
    @Test
    public void getItemListByQuestionIdAndUserIdTest() {
        List<ItemVo> itemVos = itemDao.selectItemList(testQuestionId, testUserId);
        assertThat(itemVos.get(0).getIsVoted(), equalTo(1));
        assertThat(itemVos.get(1).getIsVoted(), equalTo(0));
        assertThat(itemVos.get(2).getIsVoted(), equalTo(0));
    }
    
    @Test
    public void insertItemsTest() {
        List<ItemVo> itemVos = itemDao.selectItemList(testQuestionId, testUserId);
        assertThat(itemDao.insertItems(testQuestionId, itemVos), equalTo(itemVos.size()));
    }
    
    @Test
    public void insertItemTest() {
        
        ItemVo itemVo = new ItemVo()
                .setQuestionId(testQuestionId)
                .setTitle(testTitle)
                .setMediaTypeCode(testMediaType);
        
        assertThat(itemDao.insertItem(testQuestionId, itemVo), equalTo(1));
        
    }
    
}
