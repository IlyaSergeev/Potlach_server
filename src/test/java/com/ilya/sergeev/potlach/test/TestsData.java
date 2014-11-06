package com.ilya.sergeev.potlach.test;

import java.util.Random;

import retrofit.client.ApacheClient;

import com.ilya.sergeev.potlach.client.GiftSvcApi;
import com.ilya.sergeev.potlach.client.SecuredRestBuilder;
import com.ilya.sergeev.potlach.client.UserInfoSvcApi;
import com.ilya.sergeev.potlach.client.VoteSvcApi;

public final class TestsData
{
	private static TestsData mInstance;
	
	public static TestsData getInstance()
	{
		if (mInstance == null)
		{
			mInstance = new TestsData();
		}
		return mInstance;
	}
	
	private TestsData()
	{
	}
	
	public final static Random random = new Random();
	public final static String TEST_URL = "http://localhost:8080";
	
	public final static String USER = "user";
	public final static String USER_PASSWORD = "11111";
	
	public final static String ADMIN = "admin";
	public final static String ADMIN_PASSWORD = "12345";
	public final static String BAD_PASSWORD = "12345" + random.nextInt();
	public final static String CLIENT_ID = "mobile";
	
	public static String getUserName()
	{
		return "user_" + (int)System.currentTimeMillis(); 
	}
	
	public static String getPassword()
	{
		return getRndString("pwd_");
	}
	
	public static String getTitle()
	{
		return getRndString("title_");
	}
	
	public static String getMessage()
	{
		String message = "";
		int countWords = random.nextInt(10) + 1;
		for (int i = 0; i < countWords; i++)
		{
			message += getRndString("word_") + " ";
		}
		return message;
	}
	
	public static String getRndString(String prefix)
	{
		return prefix + random.nextInt();
	}
	
	public static UserInfoSvcApi getUserSvcApi(String name, String password)
	{
		return getSvcApi(UserInfoSvcApi.class, name, password);
	}
	
	public static GiftSvcApi getGiftsSvcApi(String name, String password)
	{
		return getSvcApi(GiftSvcApi.class, name, password);
	}
	
	public static VoteSvcApi getVoteSvcApi(String name, String password)
	{
		return getSvcApi(VoteSvcApi.class, name, password);
	}
	
	public static <T> T getSvcApi(Class<T> svcApiClass, String name, String password)
	{
		return new SecuredRestBuilder()
				.setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
				.setEndpoint(TestsData.TEST_URL)
				.setLoginEndpoint(TestsData.TEST_URL + UserInfoSvcApi.TOKEN_PATH)
				// .setLogLevel(LogLevel.FULL)
				.setUsername(name).setPassword(password).setClientId(TestsData.CLIENT_ID)
				.build().create(svcApiClass);
	}
}
