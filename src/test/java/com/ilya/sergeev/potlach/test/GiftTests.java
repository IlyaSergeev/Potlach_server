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
import com.ilya.sergeev.potlach.repository.Gift;

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
	
	@Test
	public void testSearchGifts()
	{
		String keyWord = "victory";
		List<Gift> resultGifts = Lists.newArrayList();
		int userCount = TestsData.random.nextInt(20) + 2;
		for (int i = 0; i < userCount; i++)
		{
			Gift gift = TestsData.createNewGift();
			GiftSvcApi giftApi = TestsData.createGiftApiWithNewUser();
			if (TestsData.random.nextDouble() < 0.4)
			{
				gift.setTitle(getTitleWithKeyWork(keyWord));
				gift = giftApi.createGift(gift);
				resultGifts.add(gift);
			}
			else
			{
				giftApi.createGift(gift);
			}
		}
		GiftSvcApi giftApi = TestsData.createGiftApiWithNewUser();
		Collection<Gift> giftsWithTag = giftApi.searchGift(keyWord);
		assertEquals(resultGifts.size(), giftsWithTag.size());
		for (Gift gift : resultGifts)
		{
			assertTrue(giftsWithTag.contains(gift));
			gift.setTitle(TestsData.getTitle());
		}
		
		//Delete gifts woth tag in title
		GiftSvcApi adminGiftApi = TestsData.getGiftsSvcApi(TestsData.ADMIN, TestsData.ADMIN_PASSWORD);
		for (Gift gift : resultGifts)
		{
			gift.setTitle(TestsData.getTitle());
			adminGiftApi.saveGift(gift);
		}
	}
	
	private String getTitleWithKeyWork(String keyWord)
	{
		double k = TestsData.random.nextDouble();
		if (k > 0.8)
		{
			return TestsData.random.nextInt() + " " + keyWord;
		}
		else if (k < 0.2)
		{
			return keyWord + TestsData.random.nextInt();
		}
		else
		{
			return TestsData.random.nextInt() + " " + keyWord + TestsData.random.nextInt();
		}
	}
}
