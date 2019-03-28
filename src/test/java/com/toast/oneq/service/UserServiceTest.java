package com.toast.oneq.service;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.toast.oneq.dao.UserDao;
import com.toast.oneq.vo.UserVo;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserService userService;

    @Test
    public void registerUser() throws Exception {
        UserVo user = new UserVo();
        when(userDao.insertUser(user)).thenReturn(1);
        
        int returnUserId = userService.registerUser(user);
        
        verify(userDao).insertUser(user);
        assertThat(returnUserId, equalTo(1));
    }
    
    @Test
    public void isNullUser() throws Exception {
        UserVo user = new UserVo();
        when(userDao.selectUser("1")).thenReturn(user);
        when(userDao.selectUser("100")).thenReturn(null);
        
        assertThat(userService.isNullUser("1"), equalTo(false));
        assertThat(userService.isNullUser("100"), equalTo(true));
        
        verify(userDao, times(1)).selectUser("1");
        verify(userDao, times(1)).selectUser("100");
    }
    
    @Test
    public void getUserId() throws Exception {
        UserVo newUser = new UserVo().setOAuthId("newOauthId");
        UserVo existUser = new UserVo().setOAuthId("ExistOauthid").setUserId(1);
     // TODO: 코드리뷰
        when(userDao.selectUser("ExistOauthid")).thenReturn(existUser);
        when(userService.registerUser(newUser)).thenReturn(2);
        
        assertThat(userService.getValidUserId(newUser), equalTo(2));
        assertThat(userService.getValidUserId(existUser), equalTo(1));
    }
    
    @Test
    public void isExistUserByUserIdTest() throws Exception {
        UserVo user = new UserVo();
        when(userDao.selectUser(1)).thenReturn(user);
        when(userDao.selectUser(2)).thenReturn(null);
        
        assertThat(userService.isExistUserByUserId(1), equalTo(true));
        assertThat(userService.isExistUserByUserId(2), equalTo(false));
    }

}
