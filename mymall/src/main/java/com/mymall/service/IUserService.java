package com.mymall.service;

import com.mymall.common.ServerResponse;
import com.mymall.pojo.User;

/**
 * @author zz
 *
 */
public interface IUserService {
	ServerResponse<User> login(String username,String password);
	
	public ServerResponse<String> register(User user);
	
	ServerResponse<String> checkValid(String str,String type);
}
