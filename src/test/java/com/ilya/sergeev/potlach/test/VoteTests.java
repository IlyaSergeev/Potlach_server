package com.ilya.sergeev.potlach.test;

import static org.junit.Assert.*;

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
		// TODO
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
			assertEquals(-1, voteInfo.getVotesDown());
			assertEquals(vote, voteInfo.getUserVote());
		}
	}
}
