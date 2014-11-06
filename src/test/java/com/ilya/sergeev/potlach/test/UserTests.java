package com.ilya.sergeev.potlach.test;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Test;

import com.ilya.sergeev.potlach.SimpleMessage;
import com.ilya.sergeev.potlach.client.UserInfoSvcApi;
import com.ilya.sergeev.potlach.repository.UserInfo;

public class UserTests
{
	private UserInfoSvcApi userInfoSvcAdminGood = TestsData.getUserSvcApi(TestsData.ADMIN, TestsData.ADMIN_PASSWORD);
	private UserInfoSvcApi userInfoSvcAdminBad = TestsData.getUserSvcApi(TestsData.ADMIN, TestsData.BAD_PASSWORD);
	private UserInfoSvcApi userInfoSvcGood = TestsData.getUserSvcApi(TestsData.USER, TestsData.USER_PASSWORD);
	
	@Test
	public void testGetHello() throws Exception
	{
		SimpleMessage msg = userInfoSvcAdminGood.getHello();
		assertEquals(TestsData.ADMIN, msg.getUserName());
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
			userInfoSvcAdminGood.getUserInfo("user" + TestsData.random.nextInt());
			Assert.fail("User not real");
		}
		catch (Exception ex)
		{
		}
	}
	
	@Test
	public void testCreateDeleteUserInfo()
	{
		String userName = "user" + TestsData.random.nextInt(10000);
		String password = "pass" + TestsData.random.nextInt(10000);
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
		String userName = "user" + TestsData.random.nextInt(10000);
		String password = "pass" + TestsData.random.nextInt(10000);
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
		String userName = "user" + TestsData.random.nextInt(10000);
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
		String userName = "user" + TestsData.random.nextInt(10000);
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
