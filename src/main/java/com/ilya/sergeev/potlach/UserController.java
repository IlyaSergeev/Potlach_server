package com.ilya.sergeev.potlach;

import javax.persistence.Access;
import javax.persistence.AccessType;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ilya.sergeev.potlach.client.UserInfoSvcApi;

@Controller
public class UserController
{
	// GET /user_info/name
	@RequestMapping(value = UserInfoSvcApi.USER_NAME_PATH, method = RequestMethod.GET)
	public @ResponseBody
	String getUserName()
	{
		return "It is work!!!";
	}
}
