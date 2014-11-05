package com.ilya.sergeev.potlach.client;

import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

import com.ilya.sergeev.potlach.repository.Vote;
import com.ilya.sergeev.potlach.repository.VoteInfo;

public interface VoteSvcApi
{
	public static final String VOTE_PATH = "/votes";
	
	public static final String VOTE_UP_PATH = VOTE_PATH+ "{id}/up";
	public static final String VOTE_DOWN_PATH = VOTE_PATH+ "{id}/down";
	
	public static final String ID_PARAM = "id";
	
	@GET(VOTE_PATH)
	public VoteInfo getVote(@Path(ID_PARAM)long giftId);
	
	@POST(VOTE_UP_PATH)
	public Vote sendVoteUp(@Path(ID_PARAM)long giftId);
	
	@POST(VOTE_DOWN_PATH)
	public Vote sendVoteDown(@Path(ID_PARAM)long giftId);
	
	//TODO
	//get top rate users
}
