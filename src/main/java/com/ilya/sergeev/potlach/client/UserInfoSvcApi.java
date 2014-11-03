package com.ilya.sergeev.potlach.client;

import com.ilya.sergeev.potlach.model.MockInst;

import retrofit.http.GET;

public interface UserInfoSvcApi
{
	public static final String TOKEN_PATH = "/oauth/token";
	
	public static final String USER_INFO_PATH = "/user_info";
	
	public static final String USER_NAME_PATH = USER_INFO_PATH + "/name";
	public static final String HELLO_PATH = USER_INFO_PATH + "/hello";
	
	@GET(HELLO_PATH)
	public MockInst getHello();
}
