package com.ilya.sergeev.potlach.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Test;

import com.ilya.sergeev.potlach.client.Gift;
import com.ilya.sergeev.potlach.client.GiftSvcApi;
import com.ilya.sergeev.potlach.client.UserInfo;
import com.ilya.sergeev.potlach.client.UserInfoSvcApi;
import com.ilya.sergeev.potlach.client.Vote;
import com.ilya.sergeev.potlach.client.VoteSvcApi;

public class VoteTests
{
	@Test
	public void testCreateVoteUp()
	{
		UserInfo user = TestsData.createNewUser();
		
		GiftSvcApi giftApi = TestsData.getRestAdapter(user).create(GiftSvcApi.class);
		Gift gift = giftApi.createGift(TestsData.createNewGift());
		
		VoteSvcApi voteApi = TestsData.getRestAdapter(user).create(VoteSvcApi.class);
		Vote vote = voteApi.sendVote(gift.getId(), 1);
		
		assertEquals(user.getName(), vote.getUserName());
		assertEquals(1, vote.getVote());
		assertEquals(gift.getId(), vote.getGiftId());
		
		Vote serverVote = voteApi.getVote(vote.getId());
		assertEquals(vote, serverVote);
		assertEquals(vote.getId(), serverVote.getId());
	}
	
	@Test
	public void testCreateVoteUpLarge()
	{
		UserInfo user = TestsData.createNewUser();
		
		GiftSvcApi giftApi = TestsData.getRestAdapter(user).create(GiftSvcApi.class);
		Gift gift = giftApi.createGift(TestsData.createNewGift());
		
		VoteSvcApi voteApi = TestsData.getRestAdapter(user).create(VoteSvcApi.class);
		Vote vote = voteApi.sendVote(gift.getId(), 2 + TestsData.random.nextInt(10000));
		
		assertEquals(user.getName(), vote.getUserName());
		assertEquals(1, vote.getVote());
		assertEquals(gift.getId(), vote.getGiftId());
		
		Vote serverVote = voteApi.getVote(vote.getId());
		assertEquals(vote, serverVote);
		assertEquals(vote.getId(), serverVote.getId());
	}
	
	@Test
	public void testCreateVoteDown()
	{
		UserInfo user = TestsData.createNewUser();
		
		GiftSvcApi giftApi = TestsData.getRestAdapter(user).create(GiftSvcApi.class);
		Gift gift = giftApi.createGift(TestsData.createNewGift());
		
		VoteSvcApi voteApi = TestsData.getRestAdapter(user).create(VoteSvcApi.class);
		Vote vote = voteApi.sendVote(gift.getId(), -1);
		
		assertEquals(user.getName(), vote.getUserName());
		assertEquals(-1, vote.getVote());
		assertEquals(gift.getId(), vote.getGiftId());
		
		Vote serverVote = voteApi.getVote(vote.getId());
		assertEquals(vote, serverVote);
		assertEquals(vote.getId(), serverVote.getId());
	}
	
	@Test
	public void testCreateVoteDonwLarge()
	{
		UserInfo user = TestsData.createNewUser();
		
		GiftSvcApi giftApi = TestsData.getRestAdapter(user).create(GiftSvcApi.class);
		Gift gift = giftApi.createGift(TestsData.createNewGift());
		
		VoteSvcApi voteApi = TestsData.getRestAdapter(user).create(VoteSvcApi.class);
		Vote vote = voteApi.sendVote(gift.getId(), -(2 + TestsData.random.nextInt(10000)));
		
		assertEquals(user.getName(), vote.getUserName());
		assertEquals(-1, vote.getVote());
		assertEquals(gift.getId(), vote.getGiftId());
		
		Vote serverVote = voteApi.getVote(vote.getId());
		assertEquals(vote, serverVote);
		assertEquals(vote.getId(), serverVote.getId());
	}
	
	@Test
	public void testNotSetVote()
	{
		UserInfo user = TestsData.createNewUser();
		VoteSvcApi voteApi = TestsData.getRestAdapter(user).create(VoteSvcApi.class);
		Vote serverVote = voteApi.getVote(100000 + TestsData.random.nextInt(10000));
		assertNull(serverVote);
	}
	
	@Test
	public void testTopRate()
	{
		Collection<UserInfo> users = TestsData.getRestAdapter(TestsData.USER, TestsData.USER_PASSWORD).create(UserInfoSvcApi.class).getTopUsers();
		int rating = Integer.MAX_VALUE;
		for (UserInfo user : users)
		{
			assertTrue(rating >= user.getRating());
			rating = user.getRating();
		}
		
	}
}
