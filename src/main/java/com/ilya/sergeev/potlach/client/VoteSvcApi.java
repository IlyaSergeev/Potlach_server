package com.ilya.sergeev.potlach.client;

import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

import com.ilya.sergeev.potlach.repository.VoteInfo;

public interface VoteSvcApi
{
	public static final String VOTE_PATH = "/votes";
	
	public static final String VOTE_UP_PATH = VOTE_PATH+ "/up";
	public static final String VOTE_DOWN_PATH = VOTE_PATH+ "/down";
	
	public static final String GIFT_ID_PARAM = "giftId";
	
	@GET(VOTE_PATH)
	public VoteInfo getVote(@Query(GIFT_ID_PARAM)long giftId);
	
	@POST(VOTE_UP_PATH)
	public void sendVoteUp(@Query(GIFT_ID_PARAM)long giftId);
	
	@POST(VOTE_DOWN_PATH)
	public void sendVoteDown(@Query(GIFT_ID_PARAM)long giftId);
	
	//TODO
	//get top rate users
}
