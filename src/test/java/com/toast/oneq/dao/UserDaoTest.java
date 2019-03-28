package com.toast.oneq.dao;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.toast.oneq.vo.UserVo;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextHierarchy({
    @ContextConfiguration("/spring/application-context.xml"),
    @ContextConfiguration("/spring/servlet-context.xml")
})
@Transactional
@Rollback
public class UserDaoTest {

    @Autowired
    private UserDao userDao;
    
    @Test
    public void 유저등록하고확인하기(){
        UserVo userVo = new UserVo().setOAuthId("testOauthId");
        int userId = userDao.insertUser(userVo);
        assertThat(userDao.selectUser(userId).getOAuthId(), equalTo("testOauthId"));
    }
}
