package com.ilya.sergeev.potlach;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ilya.sergeev.potlach.client.UserInfoSvcApi;
import com.ilya.sergeev.potlach.model.MockInst;

@Controller
public class UserController
{
	// GET /user_info/name
	@RequestMapping(value = UserInfoSvcApi.HELLO_PATH, method = RequestMethod.GET)
	
	public @ResponseBody MockInst getHello()
	{
		MockInst result = new MockInst();
		result.setMessage("Hello, this is test!");
		return result;
	}
}
