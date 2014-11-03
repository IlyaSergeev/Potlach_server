package com.ilya.sergeev.potlach.client;

import com.ilya.sergeev.potlach.model.SimpleMessage;

import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.POST;

public interface UserInfoSvcApi
{
	public static final String TOKEN_PATH = "/oauth/token";
	
	public static final String USER_INFO_PATH = "/user";
	
	public static final String HELLO_PATH = USER_INFO_PATH + "/hello";
	public static final String REGISTER_USER_PATH = USER_INFO_PATH + "/register";
	public static final String DELETE_USER_PATH = USER_INFO_PATH + "/delete";
	public static final String CHANGE_PASSWORD_PATH = USER_INFO_PATH + "/reset_password";
	
	@GET(HELLO_PATH)
	public SimpleMessage getHello();
	
	@POST(REGISTER_USER_PATH)
	public Response createUser(String userName, String password);
	
	@POST(REGISTER_USER_PATH)
	public Response changePassword(String userName, String newPassword);
	
	@POST(DELETE_USER_PATH)
	public Response deleteUser(String userName);
}
