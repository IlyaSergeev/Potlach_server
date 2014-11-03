package com.ilya.sergeev.potlach.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import retrofit.RestAdapter.LogLevel;
import retrofit.client.ApacheClient;

import com.ilya.sergeev.potlach.client.SecuredRestBuilder;
import com.ilya.sergeev.potlach.client.UserInfoSvcApi;
import com.ilya.sergeev.potlach.model.MockInst;

public class AuthTests
{
	private final String TEST_URL = "http://localhost:8080";
	
	private final String USERNAME1 = "admin";
	private final String USERNAME2 = "user0";
	private final String PASSWORD = "pass";
	private final String CLIENT_ID = "mobile";
	
	private UserInfoSvcApi readWriteVideoSvcUser1 = new SecuredRestBuilder()
			.setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
			.setEndpoint(TEST_URL)
			.setLoginEndpoint(TEST_URL + UserInfoSvcApi.TOKEN_PATH)
			.setLogLevel(LogLevel.FULL)
			.setUsername(USERNAME1).setPassword(PASSWORD).setClientId(CLIENT_ID)
			.build().create(UserInfoSvcApi.class);
	
	@Test
	public void testReadUserInfo() throws Exception
	{
		MockInst inst = readWriteVideoSvcUser1.getUserName();
		assertEquals("test test", inst.getMessage());
	}
}
