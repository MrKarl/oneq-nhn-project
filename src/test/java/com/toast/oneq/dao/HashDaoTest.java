package com.toast.oneq.dao;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
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

import com.toast.oneq.vo.HashVo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextHierarchy ({
        @ContextConfiguration("/spring/application-context.xml"),
        @ContextConfiguration("/spring/servlet-context.xml")
})
@Transactional
@Rollback
public class HashDaoTest {

    @Autowired
    private HashDao hashDao;
    
    private static final int testQuestionId = 3;
    
    private static final int testHashId = 3;
    private static final String testHashName = "람보르기니";
    private static final int testCnt = 3;
    private static final int secondTestHashId = 2;
    private static final String secondTestHashName = "포르쉐";
    private static final int testNewHashId = 1000;
    private static final String testNewHashName = "newHashName";
    
    private HashVo firstHashVo;    
    private HashVo secondHashVo;
    private HashVo newHashVo;
    
    @Before
    public void setUp(){
        firstHashVo = new HashVo()
                .setHashId(testHashId)
                .setHashName(testHashName)
                .setHashCount(testCnt);
        
        secondHashVo = new HashVo()
                .setHashId(secondTestHashId)
                .setHashName(secondTestHashName);
        
        newHashVo = new HashVo()
                .setHashId(testNewHashId)
                .setHashName(testNewHashName);
    }
    
    @Test
    public void getHashListTest() {        
        List<HashVo> hashVos = hashDao.getHashList(testQuestionId);
        
        assertThat(hashVos.size(), equalTo(4));
        assertThat(hashVos.get(0).getHashId(), equalTo(1));
        assertThat(hashVos.get(1).getHashId(), equalTo(24));
        assertThat(hashVos.get(2).getHashId(), equalTo(25));
        assertThat(hashVos.get(3).getHashId(), equalTo(26));
    }
    
    @Test
    public void insertHashsTest() {
        
        List<HashVo> hashVos = new ArrayList<HashVo>();
        hashVos.add(firstHashVo);
        hashVos.add(secondHashVo);
        
        assertThat(hashDao.insertHashs(testQuestionId, hashVos), equalTo(2));
        
    }
    
    @Test
    public void insertHashTest_새로운해시태그를넣을경우() {
        assertThat(hashDao.insertHash(testQuestionId, newHashVo), equalTo(2));  
    }
    
    @Test
    public void selectHashCountTest() {
        firstHashVo = hashDao.selectHashCount(firstHashVo);
        
        assertThat(firstHashVo.getHashCount(), equalTo((Integer)testCnt));
        assertThat(firstHashVo.getHashId(), equalTo((Integer)testHashId));        
    }
    
    @Test
    public void updateHashCountTest() {  
        int preHashCount = firstHashVo.getHashCount();
        assertThat(hashDao.updateHashCount(firstHashVo), equalTo(1));
        assertThat(hashDao.selectHashCount(firstHashVo).getHashCount(), equalTo(preHashCount+1));
    }
    
}
