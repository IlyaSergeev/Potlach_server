package com.ilya.sergeev.potlach;

import java.security.Principal;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.ilya.sergeev.potlach.client.UserInfoSvcApi;
import com.ilya.sergeev.potlach.repository.UserInfo;
import com.ilya.sergeev.potlach.repository.UserInfoRepository;

@Controller
public class UserController
{
	@Autowired
	UserInfoRepository mUserRepository;
	
	// GET /user_info/name
	@RequestMapping(value = UserInfoSvcApi.HELLO_PATH, method = RequestMethod.GET)
	public @ResponseBody
	SimpleMessage getHello(Principal principal)
	{
		SimpleMessage result = new SimpleMessage();
		result.setUserName(principal.getName());
		result.setMessage("Hello world");
		return result;
	}
	
	@RequestMapping(value = UserInfoSvcApi.DELETE_USER_PATH, method = RequestMethod.GET)
	public @ResponseBody
	UserInfo deleteUser(@RequestParam(UserInfoSvcApi.USER_NAME_PARAM) String userName)
	{
		return mUserRepository.findByName(userName);
	}
	
	@PreAuthorize("hasRole('ADMIN') or isAnonymous()")
	@RequestMapping(value = UserInfoSvcApi.REGISTER_USER_PATH, method = RequestMethod.POST)
	public @ResponseBody
	UserInfo createUser(@RequestParam(UserInfoSvcApi.USER_NAME_PARAM) String userName, @RequestParam(UserInfoSvcApi.PASSWORD_PARAM) String password)
	{
		UserInfo newUser = new UserInfo();
		newUser.setName(userName);
		newUser.setPassword(password);
		return mUserRepository.save(newUser);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value = UserInfoSvcApi.DELETE_USER_PATH, method = RequestMethod.POST)
	public void deleteUser(@RequestParam(UserInfoSvcApi.USER_NAME_PARAM) String userName, HttpServletResponse response)
	{
		UserInfo userInfo = mUserRepository.findByName(userName);
		if (userInfo != null && userInfo.getId() > 0)
		{
			mUserRepository.delete(userInfo);
			response.setStatus(HttpServletResponse.SC_OK);
		}
		else
		{
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value = UserInfoSvcApi.CHANGE_PASSWORD_PATH, method = RequestMethod.POST)
	public void changePassword(@RequestParam(UserInfoSvcApi.USER_NAME_PARAM) String userName, @RequestParam(UserInfoSvcApi.PASSWORD_PARAM) String newPassword, HttpServletResponse response)
	{
		UserInfo userInfo = mUserRepository.findByName(userName);
		if (userInfo != null && userInfo.getId() > 0)
		{
			userInfo.setPassword(newPassword);
			UserInfo savedUserInfo = mUserRepository.save(userInfo);
			if (savedUserInfo != null && savedUserInfo.getId() > 0)
			{
				response.setStatus(HttpServletResponse.SC_OK);
			}
			else
			{
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			}
		}
		else
		{
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}
	
	@PreAuthorize("hasRole('USER')")
	@RequestMapping(value = UserInfoSvcApi.TOP_RATE_PATH, method = RequestMethod.GET)
	public @ResponseBody Collection<String> getTopUsers()
	{
		Collection<UserInfo> users = mUserRepository.getTopRate();
		List<String> result = Lists.newArrayList();
		for (UserInfo user : users)
		{
			result.add(user.getName());
		}
		return null;
	}
}
