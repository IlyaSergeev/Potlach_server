package com.ilya.sergeev.potlach.test;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

import retrofit.client.ApacheClient;

import com.ilya.sergeev.potlach.client.SecuredRestBuilder;
import com.ilya.sergeev.potlach.client.UserInfoSvcApi;
import com.ilya.sergeev.potlach.model.SimpleMessage;
import com.ilya.sergeev.potlach.repository.UserInfo;

public class UsersTests
{
	private static Random mRnd = new Random();
	private final String TEST_URL = "http://localhost:8080";
	
	private final String TYPICAL_USERNAME = "user";
	private final String USER_PASSWORD = "11111";
	
	private final String USERNAME_ADMIN = "admin";
	private final String ADMIN_PASSWORD = "12345";
	private final String BAD_PASSWORD = "12345" + mRnd.nextInt();
	private final String CLIENT_ID = "mobile";
	
	private UserInfoSvcApi userInfoSvcAdminGood = new SecuredRestBuilder()
			.setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
			.setEndpoint(TEST_URL)
			.setLoginEndpoint(TEST_URL + UserInfoSvcApi.TOKEN_PATH)
//			.setLogLevel(LogLevel.FULL)
			.setUsername(USERNAME_ADMIN).setPassword(ADMIN_PASSWORD).setClientId(CLIENT_ID)
			.build().create(UserInfoSvcApi.class);
	
	private UserInfoSvcApi userInfoSvcAdminBad = new SecuredRestBuilder()
			.setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
			.setEndpoint(TEST_URL)
			.setLoginEndpoint(TEST_URL + UserInfoSvcApi.TOKEN_PATH)
//			.setLogLevel(LogLevel.FULL)
			.setUsername(USERNAME_ADMIN).setPassword(BAD_PASSWORD).setClientId(CLIENT_ID)
			.build().create(UserInfoSvcApi.class);
	
	private UserInfoSvcApi userInfoSvcGood = new SecuredRestBuilder()
			.setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
			.setEndpoint(TEST_URL)
			.setLoginEndpoint(TEST_URL + UserInfoSvcApi.TOKEN_PATH)
//			.setLogLevel(LogLevel.FULL)
			.setUsername(TYPICAL_USERNAME).setPassword(USER_PASSWORD).setClientId(CLIENT_ID)
			.build().create(UserInfoSvcApi.class);
	
	@Test
	public void testGetHello() throws Exception
	{
		SimpleMessage msg = userInfoSvcAdminGood.getHello();
		assertEquals(USERNAME_ADMIN, msg.getUserName());
		assertEquals("Hello world", msg.getMessage());
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
	
	@Test
	public void testGetNotRealUserInfo()
	{
		try
		{
			userInfoSvcAdminGood.getUserInfo("user" + mRnd.nextInt());
			Assert.fail("User not real");
		}
		catch (Exception ex)
		{
		}
	}
	
	@Test
	public void testCreateDeleteUserInfo()
	{
		String userName = "user" + mRnd.nextInt(10000);
		String password = "pass" + mRnd.nextInt(10000);
		UserInfo userInfo = userInfoSvcAdminGood.createUser(userName, password);
		assertEquals(userName, userInfo.getName());
		assertEquals(password, userInfo.getPassword());
		
		userInfoSvcAdminGood.deleteUser(userName);
		try
		{
			userInfo = userInfoSvcAdminGood.getUserInfo(userName);
			Assert.fail("User must be deleted");
		}
		catch (Exception ex)
		{
		}
	}
	
	@Test
	public void testCreateUserNotAdmin()
	{
		String userName = "user" + mRnd.nextInt(10000);
		String password = "pass" + mRnd.nextInt(10000);
		try
		{
			userInfoSvcGood.createUser(userName, password);
			Assert.fail("User must be deleted");
		}
		catch (Exception ex)
		{
		}
	}
	
	@Test
	public void testDeleteUserNotAdmin()
	{
		String userName = "user" + mRnd.nextInt(10000);
		try
		{
			userInfoSvcGood.deleteUser(userName);
			Assert.fail("User must be deleted");
		}
		catch (Exception ex)
		{
		}
	}
	
	@Test
	public void testChangePasswordNotAdmin()
	{
		String userName = "user" + mRnd.nextInt(10000);
		try
		{
			userInfoSvcGood.changePassword(userName, "");
			Assert.fail("User must be deleted");
		}
		catch (Exception ex)
		{
		}
	}
}
