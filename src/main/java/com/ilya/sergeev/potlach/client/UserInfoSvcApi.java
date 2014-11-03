package com.ilya.sergeev.potlach.client;

import com.ilya.sergeev.potlach.model.MockInst;

import retrofit.http.GET;

public interface UserInfoSvcApi
{
	public static final String TOKEN_PATH = "/oauth/token";
	
	public static final String USER_INFO_PATH = "/user_info";
	
	public static final String USER_NAME_PATH = USER_INFO_PATH + "/name";
	
	@GET(USER_NAME_PATH)
	public MockInst getUserName();
}
