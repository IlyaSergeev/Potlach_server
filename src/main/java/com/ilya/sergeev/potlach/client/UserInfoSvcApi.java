package com.ilya.sergeev.potlach.client;

import com.ilya.sergeev.potlach.SimpleMessage;
import com.ilya.sergeev.potlach.repository.UserInfo;

import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

public interface UserInfoSvcApi
{
	public static final String TOKEN_PATH = "/oauth/token";
	
	public static final String USER_NAME_PARAM = "username";
	public static final String PASSWORD_PARAM = "password";
	
	public static final String USER_INFO_PATH = "/users";
	
	public static final String HELLO_PATH = USER_INFO_PATH + "/hello";
	public static final String REGISTER_USER_PATH = USER_INFO_PATH + "/register";
	public static final String DELETE_USER_PATH = USER_INFO_PATH + "/delete";
	public static final String CHANGE_PASSWORD_PATH = USER_INFO_PATH + "/reset_password";
	
	@GET(HELLO_PATH)
	public SimpleMessage getHello();
	
	@GET(USER_INFO_PATH)
	public UserInfo getUserInfo(@Query(USER_NAME_PARAM) String userName);
	
	@POST(REGISTER_USER_PATH)
	public UserInfo createUser(@Query(USER_NAME_PARAM) String userName, @Query(PASSWORD_PARAM) String password);
	
	@POST(CHANGE_PASSWORD_PATH)
	public Response changePassword(@Query(USER_NAME_PARAM) String userName, @Query(PASSWORD_PARAM) String newPassword);
	
	@POST(DELETE_USER_PATH)
	public Response deleteUser(@Query(USER_NAME_PARAM) String userName); 
}
