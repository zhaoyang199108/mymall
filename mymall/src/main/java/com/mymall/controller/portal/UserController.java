package com.mymall.controller.portal;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mymall.common.Const;
import com.mymall.common.ResponseCode;
import com.mymall.common.ServerResponse;
import com.mymall.pojo.User;
import com.mymall.service.IUserService;
import com.mymall.service.impl.UserServiceImpl;
/**
 * @author zz
 *
 */
@Controller
@RequestMapping("/user/")
public class UserController {
	
	@Autowired
	private IUserService iUserService;
	/**
	 * 用户登录
	 * @param username
	 * @param password
	 * @param session
	 * @return
	 */
	@RequestMapping(value="login",method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<User> login(String username,String password,HttpSession session){
		ServerResponse<User> response = iUserService.login(username, password);
		if(response.isSuccess()){
			session.setAttribute(Const.CURRENT_USER, response.getData());
		}
		return response;
	}
	@RequestMapping(value="logout",method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String> logout(HttpSession session){
		session.removeAttribute(Const.CURRENT_USER);
		return ServerResponse.createBySuccess();
	}
	@RequestMapping(value="register",method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String> register(User user){
		return iUserService.register(user);
	}
	@RequestMapping(value="check_valid",method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String> checkValid(String str,String type){
		return iUserService.checkValid(str, type);
	}
	@RequestMapping(value="get_user_info",method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<User> getUserInfo(HttpSession session){
		User user =(User)session.getAttribute(Const.CURRENT_USER);
		if(user != null){
			return ServerResponse.createBySuccess(user);
		}
		return ServerResponse.createByErrorMessage("用户未登录，无法获得当前用户信息");
	}
	@RequestMapping(value="forget_get_question",method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String> forgetGetQuestion(String username){
		return iUserService.selectQuestion(username);
	}
	@RequestMapping(value="forget_check_answer",method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String> forgetCheckAnswer(String username,String question,String answer){
		return iUserService.checkAnswer(username, question, answer);
	}
	@RequestMapping(value="forget_reset_password",method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String> forgetResetPassword(String username,String passwordNew,String forgetToken){
		return iUserService.forgetResetPassword(username,passwordNew,forgetToken);
	}
	@RequestMapping(value="reset_password",method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<String> resetPassword(HttpSession session,String passwordOld,String passwordNew){
		User user = (User)session.getAttribute(Const.CURRENT_USER);
		if(user == null){
			ServerResponse.createByErrorMessage("用户未登录");
		}
		return iUserService.resetPassword(user, passwordOld, passwordNew);
	}
	@RequestMapping(value="update_infotmation",method = RequestMethod.POST)
	@ResponseBody
	public ServerResponse<User> updateInformation(HttpSession session,User user){
		User currentUser = (User)session.getAttribute(Const.CURRENT_USER);
		if(currentUser == null){
			ServerResponse.createByErrorMessage("用户未登录");
		}
		user.setId(currentUser.getId());
		user.setUsername(currentUser.getUsername());
		ServerResponse<User> response =  iUserService.updateInformation(user);
		if(response.isSuccess()){
			session.setAttribute(Const.CURRENT_USER, response.getData());
		}
		return response;
	}
	  @RequestMapping(value = "get_information",method = RequestMethod.POST)
	    @ResponseBody
	    public ServerResponse<User> get_information(HttpSession session){
	        User currentUser = (User)session.getAttribute(Const.CURRENT_USER);
	        if(currentUser == null){
	            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"未登录,需要强制登录status=10");
	        }
	        return iUserService.getInformation(currentUser.getId());
	    }
}
