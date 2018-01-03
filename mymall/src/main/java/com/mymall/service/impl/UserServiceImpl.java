package com.mymall.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mymall.common.Const;
import com.mymall.common.ServerResponse;
import com.mymall.dao.UserMapper;
import com.mymall.pojo.User;
import com.mymall.service.IUserService;
import com.mymall.util.MD5Util;
@Service("iUserService")
public class UserServiceImpl implements IUserService {
	@Autowired 
	private UserMapper userMapper;
	@Override
	public ServerResponse<User> login(String username, String password) {
		int resultCount = userMapper.checkUsername(username);
		if(resultCount == 0){
			return ServerResponse.createByErrorMessage("用户名不存在");
		}
		//TODO密码登陆MD5
		String md5Password = MD5Util.MD5EncodeUtf8(password);
		User user = userMapper.selectLogin(username, md5Password);
		if(user == null){
			ServerResponse.createByErrorMessage("密码错误");
		}
		user.setPassword(StringUtils.EMPTY);
		return null;
	}
	public ServerResponse<String> register(User user){

		ServerResponse volidResponse = this.checkValid(user.getUsername(), Const.USERNAME);
		if(!volidResponse.isSuccess()){
			return volidResponse;
		}
		volidResponse = this.checkValid(user.getUsername(), Const.EMAIL);
		if(!volidResponse.isSuccess()){
			return volidResponse;
		}
		user.setRole(Const.Role.ROLE_CUSTOMER);
		//MD5加密
		user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
		int resultCount = userMapper.insert(user);
		if(resultCount == 0){
			return ServerResponse.createByErrorMessage("注册失败");
		}
		return ServerResponse.createBySuccessMessage("注册成功");
	}
	public ServerResponse<String> checkValid(String str,String type){
		//type==" " false
		if(StringUtils.isNotBlank(type)){
			if(Const.USERNAME.equals(type)){
				int resultCount = userMapper.checkUsername(str);
				if(resultCount > 0){
					return ServerResponse.createByErrorMessage("用户名已存在");
				}
			}
			if(Const.EMAIL.equals(type)){
				int resultCount = userMapper.checkEmail(str);
				if(resultCount > 0){
					return ServerResponse.createByErrorMessage("Email已存在");
				}
			}
		}else{
			return ServerResponse.createByErrorMessage("参数错误");
		}
		return ServerResponse.createBySuccessMessage("校验成功");
	}

}
