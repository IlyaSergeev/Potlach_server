package com.ilya.sergeev.potlach.test;

import java.util.List;
import java.util.Random;

import retrofit.client.ApacheClient;

import com.google.common.collect.Lists;
import com.ilya.sergeev.potlach.client.Gift;
import com.ilya.sergeev.potlach.client.GiftSvcApi;
import com.ilya.sergeev.potlach.client.SecuredRestBuilder;
import com.ilya.sergeev.potlach.client.UserInfo;
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
	
	public static List<Gift> createManyGiftsFromSomeUsers()
	{
		List<Gift> allGifts = Lists.newArrayList();
		int userCount = TestsData.random.nextInt(5) + 2;
		
		for (int i = 0; i < userCount; i++)
		{
			GiftSvcApi api = createGiftApiWithNewUser();
			allGifts.addAll(createNewGifts(api, TestsData.random.nextInt(10) + 1));
		}
		
		return allGifts;
	}
	
	public static List<Gift> createNewGifts(GiftSvcApi api, int giftCount)
	{
		List<Gift> result = Lists.newArrayList();
		for (int i = 0; i < giftCount; i++)
		{
			Gift gift = createNewGift();
			gift = api.createGift(gift);
			result.add(gift);
		}
		return result;
	}
	
	public static UserInfo createNewUser()
	{
		String userName = getUserName();
		String password = getPassword();
		UserInfoSvcApi userSvc = TestsData.getUserSvcApi(TestsData.ADMIN, TestsData.ADMIN_PASSWORD);
		return userSvc.createUser(userName, password);
	}
	
	public static GiftSvcApi createGiftApiWithNewUser()
	{
		UserInfo user = createNewUser();
		return TestsData.getGiftsSvcApi(user.getName(), user.getPassword());
	}
	
	public static Gift createNewGift()
	{
		String title = getTitle();
		String message = getMessage();
		return new Gift(title, message);
	}
}
