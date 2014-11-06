package com.ilya.sergeev.potlach.test;

import static org.junit.Assert.*;

import org.apache.http.util.TextUtils;
import org.junit.Test;

import com.ilya.sergeev.potlach.client.GiftSvcApi;
import com.ilya.sergeev.potlach.client.UserInfoSvcApi;
import com.ilya.sergeev.potlach.repository.Gift;

public class GiftTests
{
	@Test
	public void testCreateGift()
	{
		String userName = TestsData.getUserName();
		String password = TestsData.getPassword();
		UserInfoSvcApi userSvc = TestsData.getUserSvcApi(TestsData.ADMIN, TestsData.ADMIN_PASSWORD);
		userSvc.createUser(userName, password);
		
		GiftSvcApi api = TestsData.getGiftsSvcApi(userName, password);
		String title = TestsData.getTitle();
		String message = TestsData.getMessage();
		Gift gift = api.createGift(new Gift(title, message));
		
		assertEquals(title, gift.getTitle());
		assertEquals(message, gift.getMessage());
		
		Gift newGift = api.getGift(gift.getId());
		assertEquals(gift.getId(), newGift.getId());
		assertEquals(title, newGift.getTitle());
		assertEquals(message, newGift.getMessage());
	}
	
	@Test
	public void testCreateGiftWithEmptyMessage()
	{
		String userName = TestsData.getUserName();
		String password = TestsData.getPassword();
		UserInfoSvcApi userSvc = TestsData.getUserSvcApi(TestsData.ADMIN, TestsData.ADMIN_PASSWORD);
		userSvc.createUser(userName, password);
		
		GiftSvcApi api = TestsData.getGiftsSvcApi(userName, password);
		String title = TestsData.getTitle();
		String message = null;
		Gift gift = api.createGift(new Gift(title, message));
		
		assertEquals(title, gift.getTitle());
		assertTrue(TextUtils.isEmpty(gift.getMessage()));
		
		Gift newGift = api.getGift(gift.getId());
		assertEquals(title, newGift.getTitle());
		assertTrue(TextUtils.isEmpty(newGift.getMessage()));
	}
	
	@Test
	public void testLoadData()
	{
		//TODO
	}
	
	@Test
	public void testGetMyGifts()
	{
		//TODO
	}
	
	@Test
	public void testGetAllGifts()
	{
		//TODO
	}
}
