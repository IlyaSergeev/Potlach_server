package com.ilya.sergeev.potlach.test;

import org.junit.Test;

import retrofit.RestAdapter;

import com.ilya.sergeev.potlach.client.Gift;
import com.ilya.sergeev.potlach.client.GiftInfo;
import com.ilya.sergeev.potlach.client.GiftSvcApi;
import com.ilya.sergeev.potlach.client.Touch;
import com.ilya.sergeev.potlach.client.TouchSvcApi;
import com.ilya.sergeev.potlach.client.UserInfo;
import static org.junit.Assert.*;

public class TouchTests
{
	@Test
	public void testCreateTouch()
	{
		UserInfo user = TestsData.createNewUser();
		RestAdapter adapter = TestsData.getRestAdapter(user);
		GiftSvcApi giftApi = adapter.create(GiftSvcApi.class);
		TouchSvcApi touchApi = adapter.create(TouchSvcApi.class);
		
		Gift gift = giftApi.createGift(TestsData.createNewGift());
		
		GiftInfo giftInfo = giftApi.getGift(gift.getId());
		
		assertFalse(giftInfo.isWasTouched());
		
		Touch touch = touchApi.touch(gift.getId());
		
		assertEquals(gift.getId(), touch.getGiftId());
		assertEquals(user.getName(), touch.getUserName());
		
		giftInfo = giftApi.getGift(gift.getId());
		
		assertTrue(giftInfo.isWasTouched());
	}
}
