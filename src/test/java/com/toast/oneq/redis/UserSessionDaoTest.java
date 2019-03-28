package com.toast.oneq.redis;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextHierarchy({
    @ContextConfiguration("/spring/application-context.xml"),
    @ContextConfiguration("/spring/servlet-context.xml")
})
public class UserSessionDaoTest {
    @Autowired
    UserSessionDao userSessionDao;
    
    @Test
    public void 삽입하고확인하고지우고확인하기() throws Exception {
        userSessionDao.appendUserId("test-uuid", 1);
        assertThat(userSessionDao.getUserId("test-uuid"), equalTo(1));
        userSessionDao.deleteUuid("test-uuid");
        assertThat(userSessionDao.isValidUuid("test-uuid"), equalTo(false));
    }
}
