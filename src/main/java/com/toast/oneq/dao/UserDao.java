package com.toast.oneq.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.toast.oneq.sharding.MultiSqlSessionTemplate;
import com.toast.oneq.vo.UserVo;


/**
 * Copyright 2016 NHN Entertainment Corp. All rights Reserved.
 * NHN Entertainment PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * tb_user에 접근하기 위한 DAO
 * @author ${email}
 */

@Repository
public class UserDao {
    private static final String NAMESPACE = "userDao.";
    
    @Autowired
    private MultiSqlSessionTemplate multiSqlSessionTemplate;

	/**
	 * 유저를 DB에 저장하기 위한 메소드
	 * @param 
	 * 	user : 저장하기 위한 유저의 정보가 들어있는 UserVO
	 * @return {int} : insert 된 유저의 U_ID
	 * 	
	 **/	
	public int insertUser(UserVo user) {
	    multiSqlSessionTemplate.insert(NAMESPACE + "insertUser",user);
	    return user.getUserId();
	}
	
	/**
	 * 인증된 유저인지 확인하기 위한 메소드
	 * @param 
	 * 	U_ID : 확인하고자하는 유저의 U_ID
	 * @return
	 * 	인증이 되어있는 유저일 경우 인증된 유저의 UserVO, 아닐 경우 null을 출력해서 없는 유저임을 알린다.
	 **/
	public UserVo selectUser(String oauthId) {
		return multiSqlSessionTemplate.getAnyShard().selectOne(NAMESPACE + "selectUserByOauthId", oauthId);
	}
	
	public UserVo selectUser(int userId){
	    return multiSqlSessionTemplate.getAnyShard().selectOne(NAMESPACE + "selectUserByUid", userId);
	}
}
