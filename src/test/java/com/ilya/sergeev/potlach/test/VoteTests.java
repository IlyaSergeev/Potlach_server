package com.ilya.sergeev.potlach.test;

import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.Test;

import com.ilya.sergeev.potlach.client.GiftSvcApi;
import com.ilya.sergeev.potlach.client.VoteSvcApi;
import com.ilya.sergeev.potlach.repository.Gift;
import com.ilya.sergeev.potlach.repository.UserInfo;
import com.ilya.sergeev.potlach.repository.Vote;
import com.ilya.sergeev.potlach.repository.VoteInfo;

public class VoteTests
{
	@Test
	public void testCreateVoteUp()
	{
		UserInfo user = TestsData.createNewUser();
		
		GiftSvcApi giftApi = TestsData.getGiftsSvcApi(user.getName(), user.getPassword());
		Gift gift = giftApi.createGift(TestsData.createNewGift());
		
		VoteSvcApi voteApi = TestsData.getVoteSvcApi(user.getName(), user.getPassword());
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
		
		GiftSvcApi giftApi = TestsData.getGiftsSvcApi(user.getName(), user.getPassword());
		Gift gift = giftApi.createGift(TestsData.createNewGift());
		
		VoteSvcApi voteApi = TestsData.getVoteSvcApi(user.getName(), user.getPassword());
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
		
		GiftSvcApi giftApi = TestsData.getGiftsSvcApi(user.getName(), user.getPassword());
		Gift gift = giftApi.createGift(TestsData.createNewGift());
		
		VoteSvcApi voteApi = TestsData.getVoteSvcApi(user.getName(), user.getPassword());
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
		
		GiftSvcApi giftApi = TestsData.getGiftsSvcApi(user.getName(), user.getPassword());
		Gift gift = giftApi.createGift(TestsData.createNewGift());
		
		VoteSvcApi voteApi = TestsData.getVoteSvcApi(user.getName(), user.getPassword());
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
		VoteSvcApi voteApi = TestsData.getVoteSvcApi(user.getName(), user.getPassword());
		Vote serverVote = voteApi.getVote(100000 + TestsData.random.nextInt(10000));
		assertNull(serverVote);
	}
	
	@Test
	public void testGetAllVotes()
	{
		UserInfo user = TestsData.createNewUser();
		Gift gift = TestsData.createNewGift();
		gift = TestsData.getGiftsSvcApi(user.getName(), user.getPassword()).createGift(gift);
		
		int downVotes = 0;
		int upVotes = 0;
		for (int i = 0; i < 100; i++)
		{
			UserInfo newUser = TestsData.createNewUser();
			VoteSvcApi voteApi = TestsData.getVoteSvcApi(newUser.getName(), newUser.getPassword());
			int voteValue = 0;
			if (TestsData.random.nextInt() % 2 > 0)
			{
				upVotes++;
				voteValue = 1;
			}
			else
			{
				downVotes++;
				voteValue = -1;
			}
			voteApi.sendVote(gift.getId(), voteValue);
			
		}
		
		VoteSvcApi voteApi = TestsData.getVoteSvcApi(user.getName(), user.getPassword());
		VoteInfo voteInfo = voteApi.getVoteOfGift(gift.getId());
		
		assertEquals(downVotes, voteInfo.getVotesDown());
		assertEquals(upVotes, voteInfo.getVotesUp());
	}
	
	@Test
	public void testCreateAllVoteWithDoubles()
	{
		UserInfo user = TestsData.createNewUser();
		
		GiftSvcApi giftApi = TestsData.getGiftsSvcApi(user.getName(), user.getPassword());
		Gift gift = giftApi.createGift(TestsData.createNewGift());
		
		VoteSvcApi voteApi = TestsData.getVoteSvcApi(user.getName(), user.getPassword());
		Vote vote = null;
		
		for (int i = 0; i < 100; i++)
		{
			vote = voteApi.sendVote(gift.getId(), 1);
			VoteInfo voteInfo = voteApi.getVoteOfGift(gift.getId());
			assertEquals(1, voteInfo.getVotesUp());
			assertEquals(0, voteInfo.getVotesDown());
			assertEquals(vote, voteInfo.getUserVote());
		}
		
		for (int i = 0; i < 100; i++)
		{
			vote = voteApi.sendVote(gift.getId(), -1);
			VoteInfo voteInfo = voteApi.getVoteOfGift(gift.getId());
			assertEquals(0, voteInfo.getVotesUp());
			assertEquals(1, voteInfo.getVotesDown());
			assertEquals(vote, voteInfo.getUserVote());
		}
	}
	
	@Test
	public void testTopRate()
	{
		Collection<UserInfo> users = TestsData.getUserSvcApi(TestsData.USER, TestsData.USER_PASSWORD).getTopUsers();
		int rating = Integer.MAX_VALUE;
		for (UserInfo user : users)
		{
			assertTrue(rating >= user.getRating());
			rating = user.getRating();
		}
		
	}
}
