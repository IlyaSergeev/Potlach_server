package com.ilya.sergeev.potlach;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ilya.sergeev.potlach.client.UserInfoSvcApi;
import com.ilya.sergeev.potlach.model.SimpleMessage;

@Controller
public class UserController
{
	// GET /user_info/name
	@RequestMapping(value = UserInfoSvcApi.HELLO_PATH, method = RequestMethod.GET)
	public @ResponseBody SimpleMessage getHello(Principal principal)
	{
		SimpleMessage result = new SimpleMessage();
		result.setUserName(principal.getName());
		result.setMessage("Hello world");
		return result;
	}
}
