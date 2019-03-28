package com.toast.oneq.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toast.oneq.dao.UserDao;
import com.toast.oneq.vo.UserVo;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;
    
    public int registerUser(UserVo user){
        return userDao.insertUser(user);
    }
    
    //Want To Fix : isNullUser -> isValidUser ( Negative to Positive)
    boolean isNullUser(String oauthId){
        return userDao.selectUser(oauthId) == null;
    }
    
    public int getValidUserId(UserVo user) throws NullPointerException { // TODO: 코드리뷰
        if( isNullUser( user.getOAuthId() ) ){
            return registerUser(user);
        } else {
            return userDao.selectUser(user.getOAuthId()).getUserId();
        }
    }
    
    // Find User By UserId
    // 2016.02.04   Panki Park
    public boolean isExistUserByUserId(int userId) { // TODO: 코드리뷰
        return userDao.selectUser(userId) != null;
    }
}