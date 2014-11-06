package com.ilya.sergeev.potlach.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.util.TextUtils;
import org.junit.Test;

import retrofit.client.Response;
import retrofit.mime.TypedFile;

import com.google.common.collect.Lists;
import com.ilya.sergeev.potlach.ImageStatus;
import com.ilya.sergeev.potlach.ImageStatus.ImageState;
import com.ilya.sergeev.potlach.client.GiftSvcApi;
import com.ilya.sergeev.potlach.client.UserInfoSvcApi;
import com.ilya.sergeev.potlach.repository.Gift;
import com.ilya.sergeev.potlach.repository.UserInfo;

public class GiftTests
{
	@Test
	public void testCreateGift()
	{
		GiftSvcApi api = TestsData.createGiftApiWithNewUser();
		
		Gift origin = TestsData.createNewGift();
		Gift gift = api.createGift(origin);
		
		assertEquals(origin.getTitle(), gift.getTitle());
		assertEquals(origin.getMessage(), gift.getMessage());
		
		Gift newGift = api.getGift(gift.getId());
		assertEquals(gift, newGift);
	}
	
	@Test
	public void testCreateGiftWithEmptyMessage()
	{
		GiftSvcApi api = TestsData.createGiftApiWithNewUser();
		
		String title = TestsData.getTitle();
		
		Gift origin = new Gift(title, null);
		Gift gift = api.createGift(origin);
		
		assertEquals(origin.getTitle(), gift.getTitle());
		assertTrue(TextUtils.isEmpty(gift.getMessage()));
		
		Gift newGift = api.getGift(gift.getId());
		assertEquals(gift, newGift);
	}
	
	@Test
	public void testLoadData() throws Exception
	{
		GiftSvcApi api = TestsData.createGiftApiWithNewUser();
		Gift gift = api.createGift(TestsData.createNewGift());
		File imageFile = new File("src/test/res/image1.jpg");
		
		ImageStatus status = api.setImageData(gift.getId(), new TypedFile(gift.getContentType(), imageFile));
		assertEquals(ImageState.READY, status.getState());
		
		Response response = api.getData(gift.getId());
		assertEquals(200, response.getStatus());
		
		InputStream videoData = response.getBody().in();
		byte[] originalFile = IOUtils.toByteArray(new FileInputStream(imageFile));
		byte[] retrievedFile = IOUtils.toByteArray(videoData);
		assertTrue(Arrays.equals(originalFile, retrievedFile));
	}
	
	@Test
	public void testGetAllGifts()
	{
		List<Gift> allGifts = TestsData.createManyGiftsFromSomeUsers();
		GiftSvcApi api = TestsData.createGiftApiWithNewUser();
		Collection<Gift> serverGifts = api.getNewGifts();
		assertTrue(serverGifts.size() > allGifts.size());
		for (Gift gift : allGifts)
		{
			assertTrue(serverGifts.contains(gift));
		}
	}
	
	@Test
	public void testGetMyGifts()
	{
		TestsData.createManyGiftsFromSomeUsers();
		
		GiftSvcApi api = TestsData.createGiftApiWithNewUser();
		List<Gift> userGifts = TestsData.createNewGifts(api, TestsData.random.nextInt(10) + 1);
		Collection<Gift> serverUserGifts = api.getMyGifts();
		assertEquals(userGifts.size(), serverUserGifts.size());
		for (Gift gift : serverUserGifts)
		{
			assertTrue(userGifts.contains(gift));
		}
	}
}
