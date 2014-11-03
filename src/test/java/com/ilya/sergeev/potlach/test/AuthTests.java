package com.ilya.sergeev.potlach.test;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.jmx.access.InvocationFailureException;

import retrofit.RestAdapter.LogLevel;
import retrofit.client.ApacheClient;

import com.ilya.sergeev.potlach.client.SecuredRestBuilder;
import com.ilya.sergeev.potlach.client.UserInfoSvcApi;
import com.ilya.sergeev.potlach.model.MockInst;

public class AuthTests
{
	private final String TEST_URL = "http://localhost:8080";
	
	private final String USERNAME_ADMIN = "admin";
	private final String PASSWORD = "12345";
	private final String BAD_PASSWORD = "12345" + (new Random()).nextInt();
	private final String CLIENT_ID = "mobile";
	
	private UserInfoSvcApi userInfoSvcAdminGood = new SecuredRestBuilder()
			.setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
			.setEndpoint(TEST_URL)
			.setLoginEndpoint(TEST_URL + UserInfoSvcApi.TOKEN_PATH)
			.setLogLevel(LogLevel.FULL)
			.setUsername(USERNAME_ADMIN).setPassword(PASSWORD).setClientId(CLIENT_ID)
			.build().create(UserInfoSvcApi.class);
	
	private UserInfoSvcApi userInfoSvcAdminBad = new SecuredRestBuilder()
			.setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
			.setEndpoint(TEST_URL)
			.setLoginEndpoint(TEST_URL + UserInfoSvcApi.TOKEN_PATH)
			.setLogLevel(LogLevel.FULL)
			.setUsername(USERNAME_ADMIN).setPassword(BAD_PASSWORD).setClientId(CLIENT_ID)
			.build().create(UserInfoSvcApi.class);
	
	@Test
	public void testGetHello() throws Exception
	{
		MockInst inst = userInfoSvcAdminGood.getHello();
		assertEquals("Hello, this is test!", inst.getMessage());
	}
	
	@Test
	public void testIncorrectAuth() throws Exception
	{
		try
		{
			userInfoSvcAdminBad.getHello();
			Assert.fail("Must be Login failed");
		}
		catch (Exception ex)
		{
		}
	}
}
