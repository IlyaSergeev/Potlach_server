package com.ilya.sergeev.potlach.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.util.TextUtils;
import org.junit.Test;

import retrofit.RestAdapter;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.ilya.sergeev.potlach.client.Gift;
import com.ilya.sergeev.potlach.client.GiftInfo;
import com.ilya.sergeev.potlach.client.GiftSvcApi;
import com.ilya.sergeev.potlach.client.ImageStatus;
import com.ilya.sergeev.potlach.client.ImageStatus.ImageState;
import com.ilya.sergeev.potlach.client.UserInfo;
import com.ilya.sergeev.potlach.client.Vote;
import com.ilya.sergeev.potlach.client.VoteSvcApi;

public class GiftTests
{
	@Test
	public void testCreateGift()
	{
		UserInfo user = TestsData.createNewUser();
		GiftSvcApi giftApi = TestsData.getRestAdapter(user).create(GiftSvcApi.class);
		
		Gift origin = TestsData.createNewGift();
		Gift gift = giftApi.createGift(origin);
		
		assertEquals(origin.getTitle(), gift.getTitle());
		assertEquals(origin.getMessage(), gift.getMessage());
		
		GiftInfo newGiftInfo = giftApi.getGift(gift.getId());
		
		assertEquals(gift, newGiftInfo.getGift());
		assertNull(newGiftInfo.getVote());
	}
	
	@Test
	public void testCreateGiftWithVote()
	{
		UserInfo user = TestsData.createNewUser();
		RestAdapter adapter = TestsData.getRestAdapter(user);
		GiftSvcApi giftApi = adapter.create(GiftSvcApi.class);
		
		Gift origin = TestsData.createNewGift();
		Gift gift = giftApi.createGift(origin);
		
		VoteSvcApi voteApi = adapter.create(VoteSvcApi.class);
		Vote vote = voteApi.sendVote(gift.getId(), 1);
		
		GiftInfo newGiftInfo = giftApi.getGift(gift.getId());
		
		assertEquals(gift, newGiftInfo.getGift());
		assertEquals(vote, newGiftInfo.getVote());
		
		
		vote = voteApi.sendVote(gift.getId(), -1);
		
		newGiftInfo = giftApi.getGift(gift.getId());
		
		assertEquals(gift, newGiftInfo.getGift());
		assertEquals(vote, newGiftInfo.getVote());
	}
	
	@Test
	public void testCreateGiftWithEmptyMessage()
	{
		UserInfo user = TestsData.createNewUser();
		GiftSvcApi giftApi = TestsData.getRestAdapter(user).create(GiftSvcApi.class);
		
		String title = TestsData.getTitle();
		
		Gift origin = new Gift(title, null);
		Gift gift = giftApi.createGift(origin);
		
		assertEquals(origin.getTitle(), gift.getTitle());
		assertTrue(TextUtils.isEmpty(gift.getMessage()));
		
		GiftInfo newGift = giftApi.getGift(gift.getId());
		
		assertEquals(gift, newGift.getGift());
		assertNull(newGift.getVote());
	}
	
	@Test
	public void testLoadData() throws Exception
	{
		UserInfo user = TestsData.createNewUser();
		GiftSvcApi giftApi = TestsData.getRestAdapter(user).create(GiftSvcApi.class);
		Gift gift = giftApi.createGift(TestsData.createNewGift());
		File imageFile = new File("src/test/res/image1.jpg");
		
		ImageStatus status = giftApi.setImageData(gift.getId(), new TypedFile(gift.getContentType(), imageFile));
		assertEquals(ImageState.READY, status.getState());
		
		Response response = giftApi.getData(gift.getId());
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
		
		UserInfo user = TestsData.createNewUser();
		GiftSvcApi giftApi = TestsData.getRestAdapter(user).create(GiftSvcApi.class);
		Collection<GiftInfo> serverGifts = giftApi.getAllGifts();
		assertTrue(serverGifts.size() > allGifts.size());
		for (Gift gift : allGifts)
		{
			boolean isFind = false;
			for (GiftInfo giftInfo : serverGifts)
			{
				if (Objects.equal(gift, giftInfo.getGift()))
				{
					isFind = true;
					break;
				}
			}
			if (!isFind)
			{
				fail("Can not find gift with id:" + gift.getId());
			}
		}
	}
	
	@Test
	public void testGetMyGifts()
	{
		TestsData.createManyGiftsFromSomeUsers();
		
		UserInfo user = TestsData.createNewUser();
		GiftSvcApi giftApi = TestsData.getRestAdapter(user).create(GiftSvcApi.class);
		List<Gift> userGifts = TestsData.createNewGifts(giftApi, TestsData.random.nextInt(10) + 1);
		Collection<GiftInfo> serverUserGifts = giftApi.getMyGifts();
		assertEquals(userGifts.size(), serverUserGifts.size());
		for (GiftInfo giftInfo : serverUserGifts)
		{
			assertTrue(userGifts.contains(giftInfo.getGift()));
		}
	}
	
	@Test
	public void testGetUserGifts()
	{
		TestsData.createManyGiftsFromSomeUsers();
		
		UserInfo user = TestsData.createNewUser();
		GiftSvcApi giftApi = TestsData.getRestAdapter(user).create(GiftSvcApi.class);
		List<Gift> userGifts = TestsData.createNewGifts(giftApi, TestsData.random.nextInt(30) + 3);
		
		UserInfo onotherUser = TestsData.createNewUser();
		GiftSvcApi newApi = TestsData.getRestAdapter(onotherUser).create(GiftSvcApi.class);
		Collection<GiftInfo> serverUserGifts = newApi.getGifts(user.getName());
		assertEquals(userGifts.size(), serverUserGifts.size());
		for (GiftInfo giftInfo : serverUserGifts)
		{
			assertTrue(userGifts.contains(giftInfo.getGift()));
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
			UserInfo user = TestsData.createNewUser();
			GiftSvcApi giftApi = TestsData.getRestAdapter(user).create(GiftSvcApi.class);
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
		UserInfo user = TestsData.createNewUser();
		GiftSvcApi giftApi = TestsData.getRestAdapter(user).create(GiftSvcApi.class);
		Collection<GiftInfo> giftsWithTag = giftApi.searchGift(keyWord);
		assertEquals(resultGifts.size(), giftsWithTag.size());
		for (Gift gift : resultGifts)
		{
			boolean wasFind = false;
			for (GiftInfo giftInfo : giftsWithTag)
			{
				if (Objects.equal(giftInfo.getGift(), gift))
				{
					wasFind = true;
					break;
				}
			}
			if (!wasFind)
			{
				fail("Not find gift with id:" + gift.getId());
			}
		}
		
		// Delete gifts woth tag in title
		GiftSvcApi adminGiftApi = TestsData.getRestAdapter(TestsData.ADMIN_USER).create(GiftSvcApi.class);
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
	
	public void testCreateTouch()
	{
		UserInfo user = TestsData.createNewUser();
		GiftSvcApi giftApi = TestsData.getRestAdapter(user).create(GiftSvcApi.class);
		Gift gift = TestsData.createNewGift();
		gift = giftApi.createGift(gift);
		
		assertEquals(0, gift.getRating());
		
		UserInfo anotherUser = TestsData.createNewUser();
		GiftSvcApi anotherGiftApi = TestsData.getRestAdapter(anotherUser).create(GiftSvcApi.class);
		
		GiftInfo serverGiftInfo = anotherGiftApi.touchGift(gift.getId());
		Gift serverGift = serverGiftInfo.getGift();
		
		assertEquals(1, serverGift.getRating());
		assertEquals(gift.getId(), serverGift.getId());
		assertEquals(gift.getTitle(), serverGift.getTitle());
		assertEquals(gift.getMessage(), serverGift.getMessage());
	}
	
	public void testCreateSomeTouchs()
	{
		UserInfo user = TestsData.createNewUser();
		GiftSvcApi giftApi = TestsData.getRestAdapter(user).create(GiftSvcApi.class);
		Gift gift = TestsData.createNewGift();
		gift = giftApi.createGift(gift);
		
		assertEquals(0, gift.getRating());
		
		UserInfo anotherUser = TestsData.createNewUser();
		GiftSvcApi anotherGiftApi = TestsData.getRestAdapter(anotherUser).create(GiftSvcApi.class);
		
		int rating = TestsData.random.nextInt(30) + 1;
		for (int i = 0; i < rating; i++)
		{
			GiftInfo serverGiftInfo = anotherGiftApi.touchGift(gift.getId());
			Gift serverGift = serverGiftInfo.getGift();
			
			assertEquals(i + 1, serverGift.getRating());
			assertEquals(gift.getId(), serverGift.getId());
			assertEquals(gift.getTitle(), serverGift.getTitle());
			assertEquals(gift.getMessage(), serverGift.getMessage());
		}
		
		GiftInfo serverGiftInfo = anotherGiftApi.getGift(gift.getId());
		Gift serverGift = serverGiftInfo.getGift();
		
		assertEquals(rating, serverGift.getRating());
		assertEquals(gift.getId(), serverGift.getId());
		assertEquals(gift.getTitle(), serverGift.getTitle());
		assertEquals(gift.getMessage(), serverGift.getMessage());
	}
}
